package org.youcode.mediconseil.web.vm.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
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
public class AvailabilityRequestVm {
    @Future(message = "Start time must be in the future")
    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @Future(message = "End time must be in the future")
    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    @NotNull(message = "Doctor ID is required")
    private UUID doctorId;

}