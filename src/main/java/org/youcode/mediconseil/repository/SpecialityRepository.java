package org.youcode.mediconseil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.youcode.mediconseil.domain.Speciality;

import java.util.Optional;
import java.util.UUID;

public interface SpecialityRepository extends JpaRepository<Speciality, UUID> {
    Optional<Speciality> findByName(String name);

}
