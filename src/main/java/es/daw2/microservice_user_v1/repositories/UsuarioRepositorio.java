package es.daw2.microservice_user_v1.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.daw2.microservice_user_v1.models.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long>{

    Optional<Usuario> findByEmail(String email);

    Page<Usuario> findAllByStatus(Usuario.UserStatus status, Pageable pageable);
}
