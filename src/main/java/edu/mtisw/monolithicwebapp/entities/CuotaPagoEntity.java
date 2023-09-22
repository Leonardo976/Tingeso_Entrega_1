package edu.mtisw.monolithicwebapp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

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

    private String tipoPago; // Cambiado de Enum a String

    private int numeroCuota;
    private double monto;
    private Date fechaVencimiento;
    private boolean pagada;

}
