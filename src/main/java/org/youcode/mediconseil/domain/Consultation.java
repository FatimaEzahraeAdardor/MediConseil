package org.youcode.mediconseil.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.youcode.mediconseil.domain.enums.ConsultationStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Future
    @NotNull
    private LocalDateTime dateConsultation;

    @Enumerated(EnumType.STRING)
    private ConsultationStatus status = ConsultationStatus.PENDING;

    private String motif;

    @ManyToOne
    @NotNull
    private Doctor doctor;

    @ManyToOne
    @NotNull
    private User patient;

    @OneToMany(mappedBy = "consultation", cascade = CascadeType.ALL)
    private List<Document> documents = new ArrayList<>();
}