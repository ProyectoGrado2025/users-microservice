package es.daw2.microservice_user_v1.exceptions;


public class ObjectNotFoundException extends RuntimeException{

    public ObjectNotFoundException(){
    }

    public ObjectNotFoundException(String message){
        super(message);
    }

    public ObjectNotFoundException (String message, Throwable cause){
        super(message, cause);
    }
}
