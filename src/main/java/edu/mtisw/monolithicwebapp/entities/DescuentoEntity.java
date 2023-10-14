package edu.mtisw.monolithicwebapp.entities;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Table(name = "descuentos")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class DescuentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull

    @Column(name = "tipo_colegio_procedencia")
    private String tipoColegioProcedencia;
    @NotNull
    private double promedioNotas;
    @NotNull
    private int anioEgreso;


    private double umbralMonto;
    @NotNull

    @Column(name = "porcentaje_descuento")
    private double porcentajeDescuento;


    @NotNull

    @Column(name = "rut_estudiante")
    private String rutEstudiante;

}

