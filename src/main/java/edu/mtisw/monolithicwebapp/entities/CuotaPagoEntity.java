package edu.mtisw.monolithicwebapp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "cuotaPago")
@NoArgsConstructor
@AllArgsConstructor
public class CuotaPagoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación ManyToOne con EstudianteEntity
    @ManyToOne
    @JoinColumn(name = "estudiante_id")
    private EstudianteEntity estudiante;

    private String tipoPago;

    private int numeroCuota;
    private double monto;
    private LocalDate fechaVencimiento;
    private boolean pagada;
    private String rutEstudiante;

    @PrePersist
    public void calculateDueDate() {


        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();


        fechaVencimiento = fechaActual.plusMonths(numeroCuota);
    }


}
