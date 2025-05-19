package es.daw2.microservice_user_v1.models.security;

import java.util.Date;

import es.daw2.microservice_user_v1.models.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class JwtToken {

    @Id @GeneratedValue
    private Long token_id;

    @Column(length = 2048)
    private String token;

    private Date expiration;

    private boolean isValid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuario user;
}