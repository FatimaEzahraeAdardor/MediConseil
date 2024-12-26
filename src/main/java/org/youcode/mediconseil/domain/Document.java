package org.youcode.mediconseil.domain;

import jakarta.persistence.*;
import lombok.*;
import org.youcode.mediconseil.domain.enums.DocumentType;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String content;
    private LocalDate dateGeneration;

    @Enumerated(EnumType.STRING)
    private DocumentType type;

    @ManyToOne
    private Consultation consultation;

}