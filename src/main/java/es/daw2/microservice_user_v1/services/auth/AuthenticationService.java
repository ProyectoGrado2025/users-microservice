package es.daw2.microservice_user_v1.services.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import es.daw2.microservice_user_v1.dto.RegisteredUser;
import es.daw2.microservice_user_v1.dto.SaveUser;
import es.daw2.microservice_user_v1.dto.auth.AuthenticationRequest;
import es.daw2.microservice_user_v1.dto.auth.AuthenticationResponse;
import es.daw2.microservice_user_v1.models.Usuario;
import es.daw2.microservice_user_v1.models.security.JwtToken;
import es.daw2.microservice_user_v1.repositories.JwtTokenRepositorio;
import es.daw2.microservice_user_v1.services.IFServicioUsuario;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IFServicioUsuario userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtTokenRepositorio jwtTokenRepositorio;

    public RegisteredUser registerOneEmployee (SaveUser newUser){
        Usuario usuario = userService.createOneEmployee(newUser);
        
        RegisteredUser userDto = new RegisteredUser();
        userDto.setId(usuario.getUser_id());
        userDto.setName(usuario.getName());
        userDto.setEmail(usuario.getEmail());
        userDto.setRole(usuario.getRole().name());
        userDto.setStatus(usuario.getStatus().name());
        return userDto;
    }

    public String generateJwt (Usuario user){
        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        return jwt;
    }

    private Map<String, Object> generateExtraClaims(Usuario user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("authorities", user.getAuthorities());

        return extraClaims;
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            authenticationRequest.getEmail(), 
            authenticationRequest.getPassword()
        );

        authenticationManager.authenticate(authentication);

        UserDetails user = userService.findByEmail(authenticationRequest.getEmail()).get();
        String jwt = jwtService.generateToken(user, generateExtraClaims((Usuario) user));
        saveUserToken((Usuario)user, jwt);
        
        AuthenticationResponse authResponse = new AuthenticationResponse();
        authResponse.setJwt(jwt);

        return authResponse;
    }

    public boolean validateToken(String jwt) {
        try{
            jwtService.extractEmail(jwt);
            return true;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }

    }

    public void logout(HttpServletRequest request) {
        String jwt = jwtService.extractJwtFromRequest(request);
        if(jwt == null || !StringUtils.hasText(jwt)){
            return;
        }
        Optional<JwtToken> token = jwtTokenRepositorio.findByToken(jwt);
        if(token.isPresent() && token.get().isValid()){
            token.get().setValid(false);
            jwtTokenRepositorio.save(token.get());
        }
    }

    private void saveUserToken(Usuario user, String jwt){
        JwtToken token = new JwtToken();
        token.setToken(jwt);
        token.setUser(user);
        token.setExpiration(jwtService.extractExpiration(jwt));
        token.setValid(true);

        jwtTokenRepositorio.save(token);
    }
}