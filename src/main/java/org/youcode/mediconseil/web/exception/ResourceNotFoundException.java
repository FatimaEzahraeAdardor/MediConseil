package org.youcode.mediconseil.web.exception;

public class ResourceNotFoundException extends RuntimeException{
   public ResourceNotFoundException(String message){
        super(message);
    }
}
