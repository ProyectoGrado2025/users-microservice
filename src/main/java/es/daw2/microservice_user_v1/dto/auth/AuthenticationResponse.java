package es.daw2.microservice_user_v1.dto.auth;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class AuthenticationResponse implements Serializable{

    @Getter @Setter
    private String jwt;
}
