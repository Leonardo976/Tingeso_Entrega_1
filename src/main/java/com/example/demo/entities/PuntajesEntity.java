package com.example.demo.entities;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "puntajes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PuntajesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cambia el tipo de datos de puntajesPruebas a List<Double> si deseas almacenar puntajes con decimales
    @Column(name = "puntajes_pruebas")
    @ElementCollection
    private List<Double> puntajesPruebas;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id")
    private EstudianteEntity estudiante;

    public double getPromedio() {
        if (puntajesPruebas.isEmpty()) {
            return 0.0; // Manejo de caso vacío
        } else {
            double suma = 0.0;
            for (Double puntaje : puntajesPruebas) {
                suma += puntaje;
            }
            return suma / puntajesPruebas.size();
        }
    }

    // Otros campos relacionados con los puntajes si es necesario

    // Constructores, getters y setters

    // Puedes agregar otros campos y métodos según tus necesidades
}
