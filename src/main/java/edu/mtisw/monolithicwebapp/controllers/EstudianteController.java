package edu.mtisw.monolithicwebapp.controllers;

import edu.mtisw.monolithicwebapp.entities.EstudianteEntity;
import edu.mtisw.monolithicwebapp.services.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
@RequestMapping
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

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


}


