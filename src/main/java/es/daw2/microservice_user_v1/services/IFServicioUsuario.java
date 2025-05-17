package es.daw2.microservice_user_v1.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.daw2.microservice_user_v1.dto.SaveUser;
import es.daw2.microservice_user_v1.models.Usuario;

public interface IFServicioUsuario {

    public abstract Page<Usuario> getAllEmployees(Pageable pageable);

    public abstract Optional<Usuario> getEmployeeById(Long id);

    public abstract Usuario updateEmployeeById(Long id, SaveUser saveUser);

    public abstract Usuario disableEmployeeById(Long id);

    public abstract Usuario createOneEmployee(SaveUser newUser);
    
    public abstract Optional<Usuario> findByEmail(String email);
}
