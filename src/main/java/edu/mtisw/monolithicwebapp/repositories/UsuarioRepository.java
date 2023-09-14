package edu.mtisw.monolithicwebapp.repositories;

import edu.mtisw.monolithicwebapp.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<UsuarioEntity, Long> {
    public UsuarioEntity findByEmail(String email);
    @Query(value = "SELECT * FROM usuarios WHERE usuarios.email = :email", nativeQuery = true)
    UsuarioEntity findByEmailNativeQuery(@Param("email") String email);

}