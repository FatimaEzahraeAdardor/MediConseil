package org.youcode.mediconseil.web.exception;

public class AlreadyExistException extends RuntimeException{
   public AlreadyExistException(String message){
        super(message);
    }
}
