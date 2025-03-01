package org.youcode.mediconseil.web.vm.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationRequestVm {
    @NotNull(message = "Date consultation is required")
    @FutureOrPresent(message = "Date consultation must be today or in the future")
    private LocalDate dateConsultation;

    @NotBlank(message = "Status is required")
    @Size(max = 50, message = "Status must not exceed 50 characters")
    private String status;

    @NotBlank(message = "Motif is required")
    @Size(min = 10, max = 500, message = "Motif must be between 10 and 500 characters")
    private String motif;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

    @NotNull(message = "User ID is required")
    private UUID userId;
}