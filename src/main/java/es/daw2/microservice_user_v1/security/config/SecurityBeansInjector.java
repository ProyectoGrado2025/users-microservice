package es.daw2.microservice_user_v1.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.daw2.microservice_user_v1.exceptions.ObjectNotFoundException;
import es.daw2.microservice_user_v1.repositories.UsuarioRepositorio;

@Configuration
public class SecurityBeansInjector {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authtenticationConfiguration) throws Exception{
        return authtenticationConfiguration.getAuthenticationManager();
    }

    // Creación de la estrategia de autenticación
    @Bean
    public AuthenticationProvider authtenticationProvider(){

        DaoAuthenticationProvider authenticationStrategy = new DaoAuthenticationProvider();
        authenticationStrategy.setPasswordEncoder( passwordEncoder() );
        authenticationStrategy.setUserDetailsService( userDetailsService() );

        return authenticationStrategy;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return (email) -> {
            return usuarioRepositorio.findByEmail(email)
                    .orElseThrow(()->new ObjectNotFoundException("Email not found with: \""+email+"\""));
        };
    }
}