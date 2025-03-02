package org.youcode.mediconseil.web.vm.response;

import lombok.Data;
import org.youcode.mediconseil.domain.enums.ConsultationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ConsultationResponseVm {
    private UUID id;
    private LocalDateTime dateConsultation;
    private String motif;
    private ConsultationStatus status;
    private UUID doctorId;
    private UUID patientId;
}