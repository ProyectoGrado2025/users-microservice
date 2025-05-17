package es.daw2.microservice_user_v1.models;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import es.daw2.microservice_user_v1.models.util.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Usuarios")

@Data
public class Usuario implements UserDetails{

    @Id @GeneratedValue
    private Long user_id;

    @Column(name = "user_fullname", nullable = false, columnDefinition = "VARCHAR(45)")
    private String name;

    @Column(unique = true, name = "user_email", nullable = false, columnDefinition = "VARCHAR(80)")
    private String email;

    @Column(name = "user_password", nullable = false, columnDefinition = "VARCHAR(200)")
    private String password;

    @Column(name = "user_rol", nullable = false, columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public static enum UserStatus{
        ENABLED, 
        DISABLED;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(role  == null) return null;
        if(role.getPermissions() == null) return null;

        /*
         * Habilitar si trabajamos con Authorities
         */
        // return role.getPermissions().stream()
        //             .map(each -> each.name())
        //             .map(each -> new SimpleGrantedAuthority(each))
        //             .collect(Collectors.toList());
        
        /*
         * Habilitar si trabajamos con Roles
         */
        List<SimpleGrantedAuthority> authorities = role.getPermissions().stream()
                    .map(each -> each.name())
                    .map(each -> new SimpleGrantedAuthority(each))
                    .collect(Collectors.toList());
        
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.role.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

}
