package es.daw2.microservice_user_v1.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SaveUser implements Serializable{

    @Size(min = 7)
    private String name;

    private String email;

    @Size(min = 8)
    private String password;

    @Size(min = 8)
    private String repeatedPassword;
}
