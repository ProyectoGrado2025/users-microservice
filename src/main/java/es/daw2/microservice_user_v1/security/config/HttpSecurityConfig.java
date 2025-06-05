package es.daw2.microservice_user_v1.security.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import es.daw2.microservice_user_v1.models.util.Role;
import es.daw2.microservice_user_v1.security.config.filters.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class HttpSecurityConfig {

    @Autowired
    private AuthenticationProvider daoAuthenticationProvider;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        return http
                // Se desactiva la protección CSRF
                // ya que no es necesaria para APIs REST stateless como JWT
                // CSRF solo en aplicaciones web que usan sesiones
                .cors(Customizer.withDefaults())
                .csrf( csrfConfig -> csrfConfig.disable() )
                // Se configura que sea una sesión sin estado
                .sessionManagement( sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS) )
                .authenticationProvider( daoAuthenticationProvider )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests( authReqConfig -> buildRequestMatchers(authReqConfig) )
                .exceptionHandling(exceptionConfig -> {
                    exceptionConfig.authenticationEntryPoint(authenticationEntryPoint);
                    exceptionConfig.accessDeniedHandler(accessDeniedHandler);
                })  
                .build();
    }

    private void buildRequestMatchers(
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authReqConfig) {
            
        /*
         * Autorización de endpoints para gestionar usuarios basado en Authorities
         */
        // authReqConfig.requestMatchers(HttpMethod.POST,"/admin/registeremployee")
        //     .hasAuthority(RolePermission.CREATE_ONE_WORKER.name());
        // authReqConfig.requestMatchers(HttpMethod.PUT,"/admin/disableemployee/{employeeId}/disable}")
        //     .hasAuthority(RolePermission.DISABLE_ONE_WORKER.name());
        // authReqConfig.requestMatchers(HttpMethod.PUT,"/admin/updateemployee/{employeeId}")
        //     .hasAuthority(RolePermission.UPDATE_ONE_WORKER.name());
        // authReqConfig.requestMatchers(HttpMethod.DELETE,"/admin/deleteemployee/{employeeId}")
        //     .hasAuthority(RolePermission.DELETE_ONE_WORKER.name());
        // authReqConfig.requestMatchers(HttpMethod.GET,"/admin/getemployee/{employeeId}")
        //     .hasAuthority(RolePermission.GET_ONE_WORKER.name());
        // authReqConfig.requestMatchers(HttpMethod.GET,"/admin/getemployees")
        //     .hasAuthority(RolePermission.GET_ALL_WORKERS.name());

        /*
         * Autorización de endpoints para gestionar usuarios basado en Roles
         */
        // authReqConfig.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST,"/admin/**")
                .hasRole(Role.ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.GET,"/admin/**")
                .hasRole(Role.ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.PUT,"/admin/**")
                .hasRole(Role.ADMINISTRATOR.name());
        authReqConfig.requestMatchers(HttpMethod.DELETE,"/admin/**")
                .hasRole(Role.ADMINISTRATOR.name());
        /*
         * Autorización de endpoints para hacer Login (Públicos)
         */
        authReqConfig.requestMatchers(HttpMethod.POST,"/auth/login").permitAll();
        authReqConfig.requestMatchers(HttpMethod.POST,"/auth/logout").permitAll();
        authReqConfig.requestMatchers(HttpMethod.GET,"/auth/validate").permitAll();
        authReqConfig.anyRequest().authenticated();
    }

    /*
    * Config CORS (Cross-Origin Resource Sharing)
    *
    * Durante el desarrollo dejar tal y como esta:
    * - setAllowedOriginPatterns("*") acepta peticiones desde cualquier origen
    * - PERO si se usa setAllowCredentials(true), no se puede usar "*", da error
    *
    * En producción:
    * - Usar setAllowedOrigins con la URL de el frontend
    * - Se evita que cualquier página pueda hacer peticiones a la API con cookies o tokens
    *
    * Si se desea ajustar:
    * - setAllowedMethods: solo GET, POST, etc., si se quiere permitir todos los métodos
    * - setAllowedHeaders: por si se necesita permitir solo ciertos headers (como Authorization).
    *
    * allowCredentials(true) es necesario si se usa JWT en header Authorization
    */
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(Arrays.asList("*"));
        // config.setAllowedOrigins(Arrays.asList(""));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}