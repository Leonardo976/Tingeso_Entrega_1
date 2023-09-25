
package edu.mtisw.monolithicwebapp.repositories;

import edu.mtisw.monolithicwebapp.entities.CuotaPagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuotaPagoRepository extends JpaRepository<CuotaPagoEntity, Long> {

    // Método para buscar todas las cuotas pendientes que no han sido pagadas
    List<CuotaPagoEntity> findByPagadaFalse();
    List<CuotaPagoEntity> findByEstudiante_Rut(String rut);
}