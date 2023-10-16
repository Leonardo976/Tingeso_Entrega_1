package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Date;
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

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        // Obtener la fecha de inicio del día en la zona horaria actual
        LocalDate fechaInicioDelDia = LocalDate.now();

        // Combina la fecha de inicio del día con la fecha de vencimiento para obtener una fecha y hora completa
        fechaVencimiento = LocalDate.from(fechaVencimiento.atTime(fechaInicioDelDia.atStartOfDay().toLocalTime()));

        // Convierte LocalDate a Date
        this.fechaVencimiento = Date.valueOf(fechaVencimiento).toLocalDate();
    }


}
