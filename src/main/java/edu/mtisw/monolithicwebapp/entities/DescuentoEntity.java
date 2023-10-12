package edu.mtisw.monolithicwebapp.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
@Entity
@Table(name = "descuentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cambiar el nombre de la propiedad
    @Column(name = "tipo_colegio_procedencia")
    private String tipoColegioProcedencia;

    private double promedioNotas;
    private int anioEgreso;


    private double umbralMonto;

    @Column(name = "porcentaje_descuento")
    private double porcentajeDescuento;

}

