package es.daw2.microservice_user_v1.exceptions;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import es.daw2.microservice_user_v1.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerGenericException(Exception exception, HttpServletRequest httpServletRequest){

        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getLocalizedMessage());
        apiError.setUrl(httpServletRequest.getRequestURL().toString());
        apiError.setMethod(httpServletRequest.getMethod());
        apiError.setTimeStamp(LocalDateTime.now());
        apiError.setMessage("Error interno en el servidor, vuelva a intentarlo");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
    
    // @ExceptionHandler(AccessDeniedException.class)
    // public ResponseEntity<?> handlerAccessDeniedException(AccessDeniedException exception, HttpServletRequest httpServletRequest){

    //     ApiError apiError = new ApiError();
    //     apiError.setBackendMessage(exception.getLocalizedMessage());
    //     apiError.setUrl(httpServletRequest.getRequestURL().toString());
    //     apiError.setMethod(httpServletRequest.getMethod());
    //     apiError.setMessage("Acceso DENEGADO. No tienes permisos necesarios para acceder a esta función. "+
    //     "Por favor, contacta con un ADMINISTRADOR si crees que esto es un error.");

    //     return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    // }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest httpServletRequest){

        ApiError apiError = new ApiError();
        apiError.setBackendMessage(exception.getLocalizedMessage());
        apiError.setUrl(httpServletRequest.getRequestURL().toString());
        apiError.setMethod(httpServletRequest.getMethod());
        apiError.setTimeStamp(LocalDateTime.now());
        apiError.setMessage("Error en la petición enviada");

        System.out.println(exception.getAllErrors().stream().map(each -> each.getDefaultMessage()).collect(Collectors.toList()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }
}
