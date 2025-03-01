package org.youcode.mediconseil.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Future
    @NotNull
    private LocalDateTime startTime;

    @Future
    @NotNull
    private LocalDateTime endTime;

    @NotNull
    private Boolean isBooked = false;

    @ManyToOne
    @NotNull
    private Doctor doctor;
}