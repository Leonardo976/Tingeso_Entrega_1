package edu.mtisw.monolithicwebapp.controllers;

import edu.mtisw.monolithicwebapp.entities.EstudianteEntity;
import edu.mtisw.monolithicwebapp.services.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @GetMapping("/list")
    public String list(Model model) {
        ArrayList<EstudianteEntity> estudiantes=estudianteService.obtenerEstudiantes();
        model.addAttribute("estudiantes",estudiantes);
        return "index";
    }

    @GetMapping("/students")
    public String getAllStudents(Model model) {
        ArrayList<EstudianteEntity> estudiantes = estudianteService.getAllEstudiantes();
        model.addAttribute("estudiantes", estudiantes);
        return "index"; // Nombre de la plantilla Thymeleaf
    }


    @GetMapping("/students/new")
    public String showStudentForm(Model model) {
        model.addAttribute("estudiante", new EstudianteEntity());
        return "estudiante-formulario";
    }

    @PostMapping("/students/save")
    public String saveStudent(@ModelAttribute EstudianteEntity estudiante) {
        estudianteService.saveStudent(estudiante);
        return "redirect:/estudiante-formulario/";
    }

}
