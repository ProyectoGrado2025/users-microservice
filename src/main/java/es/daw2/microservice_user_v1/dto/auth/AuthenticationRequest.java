package es.daw2.microservice_user_v1.dto.auth;

import java.io.Serializable;

import lombok.Data;

@Data
public class AuthenticationRequest implements Serializable{

    private String email;
    private String password;
}
