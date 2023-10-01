package edu.mtisw.monolithicwebapp.entities;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.query.Param;



import javax.persistence.*;
import java.util.List;

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

    private Long idEstudiante;

    public List<SubirDataEntity> findByRut(String rut) {
        return null;
    }




}
