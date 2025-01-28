package org.youcode.mediconseil.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(name = "content", length = 5000, nullable = false)
    private String content;

    @Column(name = "image_url")
    private String image;

    @CreationTimestamp
    private LocalDateTime created_at;

    @ManyToOne
    private Consultation consultation;

    @ManyToOne
    private Category category;

}
