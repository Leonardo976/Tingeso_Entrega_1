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




}
