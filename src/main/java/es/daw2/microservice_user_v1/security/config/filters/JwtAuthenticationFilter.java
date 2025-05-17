package es.daw2.microservice_user_v1.security.config.filters;


import java.io.IOException;
import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import es.daw2.microservice_user_v1.exceptions.ObjectNotFoundException;
import es.daw2.microservice_user_v1.models.Usuario;
import es.daw2.microservice_user_v1.models.security.JwtToken;
import es.daw2.microservice_user_v1.repositories.JwtTokenRepositorio;
import es.daw2.microservice_user_v1.services.IFServicioUsuario;
import es.daw2.microservice_user_v1.services.auth.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Autowired
    private JwtService jwtService;

    @Autowired
    private IFServicioUsuario userService;

    @Autowired
    JwtTokenRepositorio jwtTokenRepositorio;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {

        // 1. Obtener Authorization Header
        // 2. Obtener Token
        String jwt = jwtService.extractJwtFromRequest(request);
        if(jwt == null || !StringUtils.hasText(jwt)){
            filterChain.doFilter(request, response);
            return;
        }

        // 2.1. Obtener Token no expirado y válido desde la base de datos
        Optional<JwtToken> token = jwtTokenRepositorio.findByToken(jwt);
        boolean isValid = validateToken(token);

        if(!isValid){
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Obtener el Subject/Email desde el Token JWT
        // Esta acción a su vez valida el formado del Token JWT, firma y fecha de expiración
        String email = jwtService.extractEmail(jwt);

        // 4. Setear objeto Authentication dento de Security Context Holder
        Usuario user = userService.findByEmail(email)
                .orElseThrow( () -> new ObjectNotFoundException("Email Not Found. Email: "+email) );
        UsernamePasswordAuthenticationToken  authToken = new UsernamePasswordAuthenticationToken(
            email, null, user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 5. Ejecutar el registro de filtros
        filterChain.doFilter(request, response);
    }

    private boolean validateToken(Optional<JwtToken> optionalToken) {
        if(!optionalToken.isPresent()){
            System.out.println("Token no existe o no fue generado en nuestro sistema");
            return false;
        }
        JwtToken token = optionalToken.get();
        Date now = new Date(System.currentTimeMillis());

        boolean isValid = token.isValid() && token.getExpiration().after(now);

        if(!isValid){
            System.out.println("Token inválido");
            updateTokenStatus(token);
        }
        return isValid;
    }

    private void updateTokenStatus(JwtToken token) {
        token.setValid(false);
        jwtTokenRepositorio.save(token);
    }
    
}
