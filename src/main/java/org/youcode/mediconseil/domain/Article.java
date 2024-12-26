package org.youcode.mediconseil.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String content;
    private String image;
    private LocalDate created_at;

    @ManyToOne
    private Consultation consultation;

    @ManyToOne
    private Category category;

    // Getters and
}
