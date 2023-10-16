package com.example.demo.controllers;

import com.example.demo.entities.CuotaPagoEntity;
import com.example.demo.entities.DescuentoEntity;
import com.example.demo.entities.EstudianteEntity;
import com.example.demo.entities.SubirDataEntity;
import com.example.demo.repositories.CuotaPagoRepository;
import com.example.demo.repositories.DescuentoRepository;
import com.example.demo.repositories.EstudianteRepository;
import com.example.demo.services.CuotaPagoService;
import com.example.demo.services.EstudianteService;
import com.example.demo.services.GenerarCuotaService;
import com.example.demo.services.SubirDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.persistence.EntityNotFoundException;
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

    @Autowired
    private final CuotaPagoService cuotaPagoService;

    @Autowired
    private final GenerarCuotaService generarCuotaService;

    @Autowired
    @Lazy
    private SubirDataService subirDataService;
    @Autowired
    private DescuentoRepository descuentoRepository;

    @Autowired
    public EstudianteController(EstudianteRepository estudianteRepository, CuotaPagoService cuotaPagoService, GenerarCuotaService generarCuotaService) {
        this.estudianteRepository = estudianteRepository;
        this.cuotaPagoService = cuotaPagoService;
        this.generarCuotaService = generarCuotaService;
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<EstudianteEntity> estudiantes = estudianteService.obtenerEstudiantesConPuntajes();
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
        estudiante.setMatricula(70000.0);
        estudiante.setArancel(1500000.0);
        estudianteService.saveStudent(estudiante);

        // Generar y actualizar las cuotas para el estudiante
        List<CuotaPagoEntity> cuotasGeneradas = cuotaPagoService.calcularYCrearCuotasParaEstudiante(estudiante);

        // Iterar sobre las cuotas para aplicar descuentos
        for (CuotaPagoEntity cuota : cuotasGeneradas) {
            EstudianteEntity estudianteCuota = cuota.getEstudiante();
            List<DescuentoEntity> descuentos = descuentoRepository.findByAnioEgresoAndTipoColegioProcedencia(estudianteCuota.getAnioEgreso(), estudianteCuota.getTipoColegioProcedencia());

            double montoCuotaSinDescuento = cuota.getMonto();
            double montoTotalConDescuento = montoCuotaSinDescuento;

            for (DescuentoEntity descuento : descuentos) {
                double porcentajeDescuento = descuento.getPorcentajeDescuento() / 100.0;
                montoTotalConDescuento -= (montoCuotaSinDescuento * porcentajeDescuento);
            }

            cuota.setMonto(montoTotalConDescuento);
            cuotaPagoRepository.save(cuota);
        }

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
            estudianteOriginal.setNombres(updatedEstudiante.getNombres());
            estudianteOriginal.setApellidos(updatedEstudiante.getApellidos());
            // Actualiza los demás campos de manera similar
            estudianteService.saveStudent(estudianteOriginal);
            return "redirect:/list";
        } else {
            return "redirect:/list?error=true";
        }
    }

    @GetMapping("/buscar-rut")
    public String mostrarFormularioBusqueda() {
        return "buscar-rut";
    }

    @PostMapping("/buscar-rut")
    public String buscarEstudiantePorRut(@RequestParam String rut, Model model) {
        Optional<EstudianteEntity> estudianteOptional = estudianteService.buscarEstudiantePorRut(rut);
        if (estudianteOptional.isPresent()) {
            EstudianteEntity estudiante = estudianteOptional.get();
            model.addAttribute("estudiante", estudiante);
            List<CuotaPagoEntity> cuotas = cuotaPagoRepository.findByEstudiante_Rut(rut);
            model.addAttribute("cuotas", cuotas);
            return "estudiante-detalle";
        } else {
            return "redirect:/list?error=true";
        }
    }

    @GetMapping("/{id}/detalle")
    public String mostrarDetalleEstudiante(@PathVariable Long id, Model model) {
        Optional<EstudianteEntity> estudiante = estudianteRepository.findById(id);
        if (estudiante.isPresent()) {
            model.addAttribute("estudiante", estudiante.get());
            return "estudiante-detalle";
        } else {
            return "error";
        }
    }

    @PostMapping("/ingresar-estudiante")
    public String ingresarEstudiante(@RequestBody EstudianteEntity estudiante, Model model) {
        estudiante.setMatricula(70000.00);
        estudiante.setArancel(1500000.00);
        List<CuotaPagoEntity> cuotasGeneradas = cuotaPagoService.calcularYCrearCuotasParaEstudiante(estudiante);
        model.addAttribute("cuotasGeneradas", cuotasGeneradas);
        return "confirmacion";
    }

    // Endpoint para actualizar los puntajes de un estudiante por ID
    @PutMapping("/{id}/actualizar-puntajes")
    public ResponseEntity<String> actualizarPuntajesDeEstudiante(
            @PathVariable Long id,
            @RequestBody List<Integer> puntajesPruebas) {

        try {
            EstudianteEntity estudiante = estudianteService.actualizarPuntajes(id, puntajesPruebas);
            cuotaPagoService.actualizarCuotasConPuntajes(estudiante);
            return ResponseEntity.ok("Puntajes actualizados correctamente.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estudiante no encontrado con ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al actualizar los puntajes.");
        }
    }

    @PostMapping("/actualizarPuntajes")
    public ResponseEntity<String> actualizarPuntajes(
            @RequestParam("rut") String rut,
            @RequestBody List<Integer> puntajesPruebas) {

        if (rut.isEmpty() || puntajesPruebas == null || puntajesPruebas.isEmpty()) {
            return ResponseEntity.badRequest().body("El rut y los puntajes de prueba son obligatorios.");
        }

        try {
            EstudianteEntity estudiante = estudianteService.getEstudianteByRut(rut);
            if (estudiante == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Estudiante no encontrado con el rut: " + rut);
            }

            double promedio = calcularPromedioPuntajes(puntajesPruebas);
            estudianteService.actualizarPromedioPuntajes(rut, promedio);

            return ResponseEntity.ok("Promedio de puntajes actualizado correctamente para el estudiante con rut: " + rut);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al actualizar el promedio de puntajes para el estudiante con rut: " + rut);
        }
    }

    private double calcularPromedioPuntajes(List<Integer> puntajesPruebas) {
        if (puntajesPruebas == null || puntajesPruebas.isEmpty()) {
            return 0.0;
        }

        int totalPuntajes = 0;
        for (Integer puntaje : puntajesPruebas) {
            totalPuntajes += puntaje;
        }

        return (double) totalPuntajes / puntajesPruebas.size();
    }


    @GetMapping("/estudiante/{id}/promedio")
    public String mostrarPromedio(@PathVariable Long id, Model model) {
        double promedio = estudianteService.calcularPromedioService(id);
        model.addAttribute("promedio", promedio);
        return "vista_promedio";
    }

    // Endpoint para calcular y actualizar el promedio de puntajes de un estudiante por ID
    @PostMapping("/{id}/calcular-y-actualizar-promedio")
    public ResponseEntity<String> calcularYActualizarPromedio(@PathVariable Long id) {
        try {
            double promedio = estudianteService.calcularPromedioService(id);
            return ResponseEntity.ok("Promedio calculado y actualizado correctamente: " + promedio);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/cambiarEstadoPago")
    public String cambiarEstadoPago(
            @RequestParam Long cuotaId,
            @RequestParam String nuevoEstado,
            @ModelAttribute EstudianteEntity estudiante,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        cuotaPagoService.cambiarEstadoPago(cuotaId, nuevoEstado);

        // Obtén el RUT del estudiante directamente del objeto estudiante
        String estudianteRut = estudiante.getRut();

        // Agregamos un mensaje de éxito para mostrar en la página de estudiante-detalle
        redirectAttributes.addAttribute("cuotaId", cuotaId);
        redirectAttributes.addAttribute("estudianteRut", estudianteRut);

        return "redirect:/buscar-rut"; // Redirige a la página de búsqueda de estudiante
    }





}
