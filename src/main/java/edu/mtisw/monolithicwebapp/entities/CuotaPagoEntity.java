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

    @PrePersist
    public void calculateDueDate() {
        // Supongamos que las cuotas se pagan mensualmente
        // Puedes ajustar la lógica según tus requisitos

        // Obtener la fecha actual
        LocalDate fechaActual = LocalDate.now();

        // Calcular la fecha de vencimiento sumando meses al mes actual
        // Por ejemplo, aquí se suma el número de cuotas como meses a la fecha actual
        fechaVencimiento = fechaActual.plusMonths(numeroCuota);
    }
}
