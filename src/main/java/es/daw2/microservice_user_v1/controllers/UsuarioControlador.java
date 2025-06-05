package es.daw2.microservice_user_v1.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import es.daw2.microservice_user_v1.dto.RegisteredUser;
import es.daw2.microservice_user_v1.dto.SaveUser;
import es.daw2.microservice_user_v1.models.Usuario;
import es.daw2.microservice_user_v1.services.IFServicioUsuario;
import es.daw2.microservice_user_v1.services.auth.AuthenticationService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/admin")
public class UsuarioControlador {

    @Autowired
    IFServicioUsuario userService;

    @Autowired
    AuthenticationService authService;

    /**
     * Agregar en la URI:
     *  - /getemployees?page=0&size=5
     *  - page: indica que página se va a mostrar
     *  - size: el número de elementos que habrá en cada página
     * @param pageable
     * @return
     */
    @GetMapping("/empleados/listar")
    public ResponseEntity<Page<Usuario>> getEmployees(Pageable pageable) {
        Page<Usuario> employeePage = userService.getAllEmployees(pageable);
        if(employeePage.hasContent()){
            return ResponseEntity.ok(employeePage);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/empleados/listar/activos")
    public ResponseEntity<Page<Usuario>> getEnabledEmployees(Pageable pageable) {
        Page<Usuario> employeePage = userService.getEnabledEmployees(pageable);
        if(employeePage.hasContent()){
            return ResponseEntity.ok(employeePage);
        }
        return ResponseEntity.notFound().build();
    }
    

    @GetMapping("/empleados/{id}/info")
    public ResponseEntity<Usuario> getEmployeeById(@PathVariable Long id) {
        Optional<Usuario> userContainer = userService.getEmployeeById(id);
        if(userContainer.isPresent()){
            return ResponseEntity.ok(userContainer.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/empleados/{id}/actualizar")
    public ResponseEntity<Usuario> updateEmployeeById(@PathVariable Long id, @RequestBody @Valid SaveUser saveUser) {
        Usuario user = userService.updateEmployeeById(id, saveUser);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/empleados/{id}/disabled")
    public ResponseEntity<Usuario> disableEmployeeById(@PathVariable Long id) {
        Usuario user = userService.disableEmployeeById(id);

        return ResponseEntity.ok(user);
    }
    
    @PostMapping("/empleados/registrar")
    public ResponseEntity<RegisteredUser> registerOne (@RequestBody @Valid SaveUser newUser) {
        RegisteredUser registeredUser = authService.registerOneEmployee(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
}