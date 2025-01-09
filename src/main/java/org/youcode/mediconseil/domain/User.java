package org.youcode.mediconseil.domain;

import jakarta.persistence.*;
import lombok.*;
import org.youcode.mediconseil.domain.enums.Role;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String userName;
    private String emailAddress;
    private String password;
    private String city;
    private String phoneNumber;
    private String image;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user")
    private Doctor doctor;

    @OneToMany(mappedBy = "user")
    private List<Consultation> consultations ;

}