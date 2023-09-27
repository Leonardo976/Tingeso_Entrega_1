package edu.mtisw.monolithicwebapp.entities;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "data")
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Data
public class SubirDataEntity {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;
    private String rut;
    private String fechaExamen;
    private Integer puntajeObtenido;


}
