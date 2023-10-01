package edu.mtisw.monolithicwebapp.repositories;

import edu.mtisw.monolithicwebapp.entities.PuntajesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PuntajesRepository extends JpaRepository<PuntajesEntity, Long> {
    // Puedes agregar métodos de consulta personalizados aquí si es necesario
}