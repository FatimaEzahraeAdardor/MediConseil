package org.youcode.mediconseil.web.vm.response;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AvailabilityResponseVm {
    private UUID id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean isBooked;
    private UUID doctorId;
}