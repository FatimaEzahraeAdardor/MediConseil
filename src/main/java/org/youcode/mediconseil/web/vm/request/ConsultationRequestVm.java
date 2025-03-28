package org.youcode.mediconseil.web.vm.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationRequestVm {
    @NotBlank(message = "Motif is required")
    @Size(min = 10, max = 500, message = "Motif must be between 10 and 500 characters")
    private String motif;

    @NotNull(message = "Availability ID is required")
    private UUID availabilityId;

    @NotNull(message = "Date consultation is required")
    @FutureOrPresent(message = "Consultation date must be in the present or future")
    private LocalDateTime dateConsultation;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;
}