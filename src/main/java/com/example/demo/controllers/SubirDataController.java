package com.example.demo.controllers;

import com.example.demo.entities.SubirDataEntity;
import com.example.demo.services.SubirDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class SubirDataController {
    @Autowired
    private SubirDataService subirData;

    @Autowired
    private SubirDataService subirDataService;


    @GetMapping("/fileUpload")
    public String main() {
        return "fileUpload";
    }

    @PostMapping("/fileUpload")
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        subirData.guardar(file);
        redirectAttributes.addFlashAttribute("mensaje", "¡Archivo cargado correctamente!");
        subirData.leerTxt("Data.txt");
        return "redirect:/fileUpload";
    }

    @GetMapping("/fileInformation")
    public String listar(Model model) {
        ArrayList<SubirDataEntity> datas = subirData.obtenerData();
        model.addAttribute("datas", datas);
        return "fileInformation";
    }
    @PostMapping("/actualizarPromedio")
    public String actualizarPromedio(@RequestParam("rut") String rut) {
        // Obtiene los puntajes del estudiante por su rut
        List<SubirDataEntity> puntajes = subirData.obtenerPuntajesPruebasPorRut(rut);

        // Calcula el promedio de puntajes
        double promedio = calcularPromedioPuntajes(puntajes);

        // Llama al servicio para actualizar el promedio
        subirData.actualizarPromedioPuntajes(rut, promedio);

        // Redirige a donde desees después de actualizar el promedio
        return "redirect:/fileInformation";
    }

    private double calcularPromedioPuntajes(List<SubirDataEntity> puntajes) {
        if (puntajes.isEmpty()) {
            return 0.0;
        }

        int sumaPuntajes = 0;
        for (SubirDataEntity puntaje : puntajes) {
            sumaPuntajes += puntaje.getPuntajeObtenido();
        }

        return (double) sumaPuntajes / puntajes.size();
    }



    @GetMapping("/calcularPromedio/{idEstudiante}")
    public ResponseEntity<Double> calcularPromedioPorEstudiante(@PathVariable Long idEstudiante) {
        double promedio = subirDataService.calcularPromedioPuntajesPorEstudiante(idEstudiante);
        return ResponseEntity.ok(promedio);
    }
}
