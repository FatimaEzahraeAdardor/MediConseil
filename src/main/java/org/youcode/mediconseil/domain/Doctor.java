package org.youcode.mediconseil.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Doctor extends User {
    private String address;
    private String experiences;
    private String diploma;
    private String description;
    private Double price;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Availability> availabilities;

    @OneToMany(mappedBy = "doctor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Consultation> consultations;

    @ManyToOne(fetch = FetchType.LAZY)
    private Speciality speciality;

}
