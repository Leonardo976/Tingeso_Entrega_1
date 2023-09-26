package edu.mtisw.monolithicwebapp.services;

import edu.mtisw.monolithicwebapp.entities.CuotaPagoEntity;
import edu.mtisw.monolithicwebapp.entities.EstudianteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenerarCuotaService {

    private final CuotaPagoService cuotaPagoService;

    @Autowired
    public GenerarCuotaService(CuotaPagoService cuotaPagoService) {
        this.cuotaPagoService = cuotaPagoService;
    }

    public void generarCuotasParaEstudiante(EstudianteEntity estudiante) {
        // Llamada al servicio para generar cuotas
        List<CuotaPagoEntity> cuotasGeneradas = cuotaPagoService.calcularYCrearCuotasParaEstudiante(estudiante);

    }

    // Otros métodos y lógica de tu aplicación
}