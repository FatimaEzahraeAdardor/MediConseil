package org.youcode.mediconseil.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import org.youcode.mediconseil.domain.Doctor;

import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

}
