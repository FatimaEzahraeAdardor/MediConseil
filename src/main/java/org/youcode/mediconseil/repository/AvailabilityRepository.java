package org.youcode.mediconseil.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.youcode.mediconseil.domain.Article;
import org.youcode.mediconseil.domain.Availability;
import org.youcode.mediconseil.domain.Doctor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvailabilityRepository extends JpaRepository<Availability, UUID>{
    Page<Availability> findByDoctorIdAndIsBookedFalse(UUID doctorId, Pageable pageable);
    Optional<Availability> findByDoctorAndStartTime(Doctor doctor, LocalDateTime startTime);


}
