package es.daw2.microservice_user_v1.exceptions;

public class InvalidPasswordException extends RuntimeException{

    
    public InvalidPasswordException(){
    }

    public InvalidPasswordException(String message){
        super(message);
    }

    public InvalidPasswordException (String message, Throwable cause){
        super(message, cause);
    }
}
