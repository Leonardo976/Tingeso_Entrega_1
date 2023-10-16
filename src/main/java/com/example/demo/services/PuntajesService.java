package com.example.demo.services;


import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Service
public class PuntajesService {
    @PersistenceContext
    private EntityManager entityManager;

    // Método para obtener los puntajes por RUT
    public List<Double> obtenerPuntajesPorRut(String rut) {
        // Realiza una consulta SQL para seleccionar los puntajes_promedio_pruebas
        String sql = "SELECT puntaje_promedio_pruebas FROM data WHERE rut = :rut";

        // Crea la consulta
        List<Double> puntajes = entityManager.createNativeQuery(sql)
                .setParameter("rut", rut)
                .getResultList();

        return puntajes;
    }
}
