package es.daw2.microservice_user_v1.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import es.daw2.microservice_user_v1.dto.SaveUser;
import es.daw2.microservice_user_v1.exceptions.InvalidPasswordException;
import es.daw2.microservice_user_v1.exceptions.ObjectNotFoundException;
import es.daw2.microservice_user_v1.models.Usuario;
import es.daw2.microservice_user_v1.models.util.Role;
import es.daw2.microservice_user_v1.repositories.UsuarioRepositorio;

@Service
public class ImpServicioUsuario implements IFServicioUsuario{

    @Autowired
    UsuarioRepositorio userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario createOneEmployee(SaveUser newUser) {

        validatePassword(newUser);

        Usuario user = new Usuario();
        user.setName(newUser.getName());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        user.setEmail(newUser.getEmail());
        user.setStatus(Usuario.UserStatus.ENABLED);
        user.setRole(Role.WORKER);

        return userRepository.save(user);
    }

    private void validatePassword(SaveUser newUser) {

        if(!StringUtils.hasText(newUser.getPassword()) || !StringUtils.hasText(newUser.getRepeatedPassword())){
            throw new InvalidPasswordException("Passwords cannot be void");
        }

        if(!newUser.getPassword().equals(newUser.getRepeatedPassword())){
            throw new InvalidPasswordException("Passwords don't match");
        }
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Page<Usuario> getAllEmployees(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<Usuario> getEmployeeById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Usuario updateEmployeeById(Long id, SaveUser saveUser) {

        validatePassword(saveUser);

        Usuario userFromDb = userRepository.findById(id)
                                .orElseThrow(()->new ObjectNotFoundException("Employee not found with ID: " + id));
        userFromDb.setName(saveUser.getName());
        userFromDb.setEmail(saveUser.getEmail());
        userFromDb.setPassword(passwordEncoder.encode(saveUser.getPassword()));

        return userRepository.save(userFromDb);
    }

    @Override
    public Usuario disableEmployeeById(Long id) {
        Usuario userFromDb = userRepository.findById(id)
                                .orElseThrow(()->new ObjectNotFoundException("Employee not found with ID: " + id));
        userFromDb.setStatus(Usuario.UserStatus.DISABLED);
        userFromDb.setRole(Role.NONE);

        return userRepository.save(userFromDb);
    }
}