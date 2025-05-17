package es.daw2.microservice_user_v1.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class LogoutResponse implements Serializable{

    private String message;

    public LogoutResponse(String message){
        this.message = message;
    }
}
