package es.daw2.microservice_user_v1.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError implements Serializable{

    private String backendMessage;
    private String message;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private LocalDateTime timeStamp;
    private String url;
    private String method;
}
