package org.youcode.mediconseil.web.exception.consultation;

public class InvalidConsultationStatusException extends RuntimeException {
    public InvalidConsultationStatusException(String message) {
        super(message);
    }
}
