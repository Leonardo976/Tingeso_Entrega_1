package com.example.demo.controllers;

import com.example.demo.entities.PuntajesEntity;
import com.example.demo.services.PuntajesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/puntajes")
public class PuntajesController {

    private final PuntajesService puntajesService;

    @Autowired
    public PuntajesController(PuntajesService puntajesService) {
        this.puntajesService = puntajesService;
    }

    // Endpoint para obtener los puntajes por rut
    @GetMapping("/rut/{rut}")
    public List<Double> obtenerPuntajesPorRut(@PathVariable String rut) {
        return puntajesService.obtenerPuntajesPorRut(rut);
    }

    // Otros endpoints relacionados con los puntajes si es necesario

    // Puedes agregar más métodos y endpoints según tus necesidades
}
