package es.daw2.microservice_user_v1.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.daw2.microservice_user_v1.models.security.JwtToken;

public interface JwtTokenRepositorio extends JpaRepository<JwtToken, Long>{

    Optional<JwtToken> findByToken(String jwt);

}
