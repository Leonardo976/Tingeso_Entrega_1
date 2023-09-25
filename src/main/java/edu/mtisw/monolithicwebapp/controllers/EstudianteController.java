package edu.mtisw.monolithicwebapp.controllers;

import edu.mtisw.monolithicwebapp.entities.CuotaPagoEntity;
import edu.mtisw.monolithicwebapp.entities.EstudianteEntity;
import edu.mtisw.monolithicwebapp.repositories.CuotaPagoRepository;
import edu.mtisw.monolithicwebapp.repositories.EstudianteRepository;
import edu.mtisw.monolithicwebapp.services.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private CuotaPagoRepository cuotaPagoRepository;
    @Autowired
    private final EstudianteRepository estudianteRepository;

    @GetMapping("/list")
    public String list(Model model) {
        ArrayList<EstudianteEntity> estudiantes = estudianteService.obtenerEstudiantes();
        model.addAttribute("estudiantes", estudiantes);
        return "lista-estudiantes";
    }

    @GetMapping("/formulario")
    public String showStudentForm(Model model) {
        model.addAttribute("estudiante", new EstudianteEntity());
        return "formulario";
    }

    @PostMapping("/guardar")
    public String saveStudent(@ModelAttribute EstudianteEntity estudiante) {
        estudianteService.saveStudent(estudiante);
        return "redirect:/list";
    }

    @PostMapping("/eliminarEstudiante/{id}")
    public String eliminarEstudiante(@PathVariable Long id) {
        estudianteService.eliminarEstudiante(id);
        return "redirect:/list";
    }

    @GetMapping("/editarEstudiante/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<EstudianteEntity> estudianteOptional = estudianteService.obtenerPorId(id);

        if (estudianteOptional.isPresent()) {
            EstudianteEntity estudiante = estudianteOptional.get();
            model.addAttribute("estudiante", estudiante);
            return "formulario";
        } else {
            return "redirect:/list?error=true";
        }
    }

    @PostMapping("/editarEstudiante/{id}")
    public String editStudent(@PathVariable Long id, @ModelAttribute EstudianteEntity updatedEstudiante) {
        Optional<EstudianteEntity> estudianteOptional = estudianteService.obtenerPorId(id);

        if (estudianteOptional.isPresent()) {
            EstudianteEntity estudianteOriginal = estudianteOptional.get();

            // Actualizar los datos del estudiante original con los datos editados
            estudianteOriginal.setNombres(updatedEstudiante.getNombres());
            estudianteOriginal.setApellidos(updatedEstudiante.getApellidos());
            // Actualiza los demás campos de manera similar

            // Guardar el estudiante original con los datos actualizados
            estudianteService.saveStudent(estudianteOriginal);

            return "redirect:/list";
        } else {
            return "redirect:/list?error=true";
        }
    }

    @GetMapping("/buscar-rut")
    public String mostrarFormularioBusqueda() {
        return "buscar-rut"; // Nombre de la vista de búsqueda por Rut
    }

    @PostMapping("/buscar-rut")
    public String buscarEstudiantePorRut(@RequestParam String rut, Model model) {
        // Realizar la búsqueda del estudiante por Rut en tu servicio
        Optional<EstudianteEntity> estudianteOptional = estudianteService.buscarEstudiantePorRut(rut);

        if (estudianteOptional.isPresent()) {
            EstudianteEntity estudiante = estudianteOptional.get();

            // Agregar el estudiante al modelo para pasar sus datos a la vista
            model.addAttribute("estudiante", estudiante);

            return "estudiante-detalle"; // Nombre de la nueva vista que muestra los detalles del estudiante
        } else {
            return "redirect:/list?error=true"; // Redirigir en caso de no encontrar al estudiante
        }
    }

    @GetMapping("/estudiante-detalle/{rut}")
    public String showEstudianteDetalle(@PathVariable String rut, Model model) {
        Optional<EstudianteEntity> estudianteOptional = estudianteService.buscarEstudiantePorRut(rut);

        if (estudianteOptional.isPresent()) {
            EstudianteEntity estudiante = estudianteOptional.get();
            model.addAttribute("estudiante", estudiante);

            // Obtener las cuotas del estudiante por su Rut utilizando la consulta personalizada
            List<CuotaPagoEntity> cuotas = cuotaPagoRepository.findByEstudiante_Rut(rut);
            model.addAttribute("cuotas", cuotas);

            return "estudiante-detalle"; // Nombre de la vista que muestra los detalles del estudiante y sus cuotas
        } else {
            return "redirect:/list?error=true";
        }
    }

    @Autowired
    public EstudianteController(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    @GetMapping("/{id}/detalle")
    public String mostrarDetalleEstudiante(@PathVariable Long id, Model model) {
        // Obtener el estudiante por ID (las cuotas de pago se cargarán automáticamente)
        Optional<EstudianteEntity> estudiante = estudianteRepository.findById(id);

        if (estudiante.isPresent()) {
            // Agregar el estudiante al modelo (incluyendo sus cuotas de pago)
            model.addAttribute("estudiante", estudiante.get());
            return "estudiante-detalle";
        } else {
            // Manejar el caso en que el estudiante no existe
            return "error"; // Página de error o redirección apropiada
        }
    }



}


