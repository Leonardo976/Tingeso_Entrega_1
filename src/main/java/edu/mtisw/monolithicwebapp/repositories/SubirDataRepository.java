package edu.mtisw.monolithicwebapp.repositories;

import edu.mtisw.monolithicwebapp.entities.SubirDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubirDataRepository extends JpaRepository<SubirDataEntity, Integer> {

    @Query("SELECT s FROM SubirDataEntity s WHERE s.rut = :rut AND s.fechaExamen = :fechaExamen")
    Optional<SubirDataEntity> findByRutAndFechaExamen(@Param("rut") String rut, @Param("fechaExamen") String fechaExamen);

    @Query("SELECT DISTINCT s.rut FROM SubirDataEntity s")
    List<String> findDistinctRut();

    @Query("SELECT s.fechaExamen FROM SubirDataEntity s WHERE s.rut = :rut")
    String findFechaExamenByRut(@Param("rut") String rut);

    @Query("SELECT s FROM SubirDataEntity s WHERE s.rut = :rut")
    ArrayList<SubirDataEntity> deleteByRut(@Param("rut") String rut);

    // Agregar consulta para encontrar por puntajeObtenido
    @Query("SELECT s FROM SubirDataEntity s WHERE s.puntajeObtenido = :puntajeObtenido")
    List<SubirDataEntity> findByPuntajeObtenido(@Param("puntajeObtenido") Integer puntajeObtenido);

    List<SubirDataEntity> findByRut(String rut);
}
