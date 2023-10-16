package com.example.demo.repositories;
import com.example.demo.entities.EstudianteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteRepository extends CrudRepository<EstudianteEntity, Long> {
    public EstudianteEntity findByRut(String rut);

    @Query(value = "SELECT * FROM estudiantes WHERE estudiantes.rut = :rut", nativeQuery = true)
    EstudianteEntity findByRutNativeQuery(@Param("rut") String rut);







}

