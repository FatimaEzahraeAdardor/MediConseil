package org.youcode.mediconseil.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    private LocalDate dateConsultation;
    private String status;
    private String motif;

    @ManyToOne
    private Doctor doctor;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "consultation")
    private List<Document> documents ;

    @OneToMany(mappedBy = "consultation")
    private List<Article> articles ;

}