package es.daw2.microservice_user_v1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.daw2.microservice_user_v1.dto.LogoutResponse;
import es.daw2.microservice_user_v1.dto.auth.AuthenticationRequest;
import es.daw2.microservice_user_v1.dto.auth.AuthenticationResponse;
import es.daw2.microservice_user_v1.services.auth.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestParam String jwt) {
        boolean isTokenValid = authenticationService.validateToken(jwt);
        return ResponseEntity.ok(isTokenValid);
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate (@RequestBody @Valid AuthenticationRequest authenticationRequest){

        AuthenticationResponse authResponse = authenticationService.login(authenticationRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletRequest request) {
        authenticationService.logout(request);
        return ResponseEntity.ok(new LogoutResponse("Logout exitoso."));
    }
    

}
