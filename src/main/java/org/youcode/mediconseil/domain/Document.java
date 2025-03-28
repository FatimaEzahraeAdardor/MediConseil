package org.youcode.mediconseil.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.youcode.mediconseil.domain.enums.DocumentType;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDateTime dateGeneration;
    private String fileName;
    private String fileUrl;
    private long fileSize;
    @Enumerated(EnumType.STRING)
    private DocumentType type;

    @ManyToOne
    @JsonIgnore // Empêche la boucle infinie
    private Consultation consultation;

}