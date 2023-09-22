package edu.mtisw.monolithicwebapp.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "estudiantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rut;
    private String apellidos;
    private String nombres;
    private String fechaNacimiento;
    private String tipoColegioProcedencia;
    private String nombreColegio;
    private int anioEgresoColegio;


    // Agregar la lista de puntajes de pruebas
    @ElementCollection
    @CollectionTable(name = "puntajes_pruebas", joinColumns = @JoinColumn(name = "estudiante_id"))
    @Column(name = "puntaje")
    private List<Integer> puntajesPruebas;

    // Método para calcular el puntaje promedio de las pruebas
    public double getPuntajePromedioPruebas() {
        List<Integer> puntajes = getPuntajesPruebas();
        if (puntajes != null && !puntajes.isEmpty()) {
            OptionalDouble promedio = puntajes.stream()
                    .mapToDouble(Integer::doubleValue)
                    .average();
            return promedio.orElse(0.0); // Valor por defecto si no hay puntajes
        }
        return 0.0; // Valor por defecto si no hay puntajes
    }

}
