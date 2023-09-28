
package edu.mtisw.monolithicwebapp.repositories;

import edu.mtisw.monolithicwebapp.entities.CuotaPagoEntity;
import edu.mtisw.monolithicwebapp.entities.EstudianteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuotaPagoRepository extends JpaRepository<CuotaPagoEntity, Long> {


    List<CuotaPagoEntity> findByPagadaFalse();
    List<CuotaPagoEntity> findByEstudiante_Rut(String rut);
    List<CuotaPagoEntity> findByEstudiante(EstudianteEntity estudiante);
}