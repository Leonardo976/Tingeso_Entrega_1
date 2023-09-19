package edu.mtisw.monolithicwebapp.repositories;
import edu.mtisw.monolithicwebapp.entities.EstudianteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteRepository extends CrudRepository<EstudianteEntity, Long> {
    public EstudianteEntity findByRut(String rut);

    @Query(value = "SELECT * FROM students WHERE students.rut = :rut", nativeQuery = true)
    EstudianteEntity findByRutNativeQuery(@Param("rut") String rut);
}

