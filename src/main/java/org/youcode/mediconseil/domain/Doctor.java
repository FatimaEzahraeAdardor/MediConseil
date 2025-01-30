package org.youcode.mediconseil.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String address;
    private String experiences;
    private String diploma;
    private String description;
    private Double price;

    @OneToMany(mappedBy = "doctor")
    private List<Availability> availabilities ;

    @OneToMany(mappedBy = "doctor")
    private List<Consultation> consultations;
    @OneToOne
    private User user;
    @ManyToOne
    private Speciality specialtie;
}
