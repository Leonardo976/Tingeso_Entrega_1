
package com.example.demo.repositories;

import com.example.demo.entities.CuotaPagoEntity;
import com.example.demo.entities.EstudianteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface CuotaPagoRepository extends JpaRepository<CuotaPagoEntity, Long> {


    List<CuotaPagoEntity> findByPagadaFalse();
    List<CuotaPagoEntity> findByEstudiante_Rut(String rut);

    List<CuotaPagoEntity> findByEstudiante(EstudianteEntity estudiante);

    List<CuotaPagoEntity> findByEstudianteAndPagadaFalse(EstudianteEntity estudiante);

    List<CuotaPagoEntity> findByRutEstudiante(String rutEstudiante);

    @Query("SELECT c FROM CuotaPagoEntity c WHERE c.estudiante.rut = :rutEstudiante AND c.pagada = false")
    List<CuotaPagoEntity> findPendientesByEstudiante(@Param("rutEstudiante") String rutEstudiante);




}