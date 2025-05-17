package es.daw2.microservice_user_v1.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class RegisteredUser implements Serializable {

    private Long id;

    private String name;

    private String email;

    private String role;

    private String status;
}
