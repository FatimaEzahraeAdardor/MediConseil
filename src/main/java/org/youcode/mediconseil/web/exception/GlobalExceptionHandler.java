package org.youcode.mediconseil.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.youcode.mediconseil.web.exception.consultation.ConsultationDateMismatchException;
import org.youcode.mediconseil.web.exception.consultation.DoctorMismatchException;
import org.youcode.mediconseil.web.exception.consultation.InvalidConsultationStatusException;
import org.youcode.mediconseil.web.exception.consultation.TimeSlotAlreadyBookedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleIInvalidObjectExeption(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<String> handleAlreadyExistException(AlreadyExistException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TimeSlotAlreadyBookedException.class)
    public ResponseEntity<String> handleTimeSlotAlreadyBookedException(TimeSlotAlreadyBookedException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(DoctorMismatchException.class)
    public ResponseEntity<String> handleDoctorMismatchException(DoctorMismatchException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ConsultationDateMismatchException.class)
    public ResponseEntity<String> handleConsultationDateMismatchException(ConsultationDateMismatchException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(InvalidConsultationStatusException.class)
    public ResponseEntity<String> handleInvalidConsultationStatusException(InvalidConsultationStatusException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }



}
