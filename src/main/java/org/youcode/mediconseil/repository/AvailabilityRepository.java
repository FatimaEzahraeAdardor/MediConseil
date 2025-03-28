package org.youcode.mediconseil.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    Optional<Availability> findByDoctorIdAndStartTime(UUID doctorId, LocalDateTime startTime);
    // Trouver toutes les disponibilités d'un médecin qui ne sont pas réservées
    List<Availability> findByDoctorIdAndIsBookedFalseOrderByStartTimeAsc(UUID doctorId);

    // Trouver les disponibilités d'un médecin pour une date spécifique
    @Query("SELECT a FROM Availability a WHERE a.doctor.id = :doctorId AND DATE(a.startTime) = :date AND a.isBooked = false ORDER BY a.startTime")
    List<Availability> findAvailableSlotsByDoctorAndDate(
            @Param("doctorId") UUID doctorId,
            @Param("date") LocalDate date);


}
