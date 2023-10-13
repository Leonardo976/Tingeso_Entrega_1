package edu.mtisw.monolithicwebapp.repositories;

import edu.mtisw.monolithicwebapp.entities.DescuentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DescuentoRepository extends JpaRepository<DescuentoEntity, Long> {
    List<DescuentoEntity> findByAnioEgreso(int anioEgreso);
    List<DescuentoEntity> findByTipoColegioProcedencia(String tipoColegioProcedencia);
    List<DescuentoEntity> findByPromedioNotasGreaterThanEqual(double promedio);

    @Query("SELECT d FROM DescuentoEntity d WHERE d.anioEgreso = :anioEgreso AND d.tipoColegioProcedencia = :tipoColegioProcedencia")
    List<DescuentoEntity> findByAnioEgresoAndTipoColegioProcedencia(@Param("anioEgreso") int anioEgreso, @Param("tipoColegioProcedencia") String tipoColegioProcedencia);

    List<DescuentoEntity> findByRutEstudiante(String rutEstudiante);




}
