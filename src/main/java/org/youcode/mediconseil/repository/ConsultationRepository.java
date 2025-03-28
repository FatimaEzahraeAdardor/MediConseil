package org.youcode.mediconseil.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.youcode.mediconseil.domain.Consultation;
import org.youcode.mediconseil.domain.enums.ConsultationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {

    // Add these new methods
    @Query("SELECT c FROM Consultation c WHERE c.patient.id = :patientId")
    Page<Consultation> findByPatientId(@Param("patientId") UUID patientId, Pageable pageable);

    @Query("SELECT c FROM Consultation c WHERE c.doctor.id = :doctorId")
    Page<Consultation> findByDoctorId(@Param("doctorId") UUID doctorId, Pageable pageable);
    List<Consultation> findByStatusAndDateConsultationLessThan(ConsultationStatus status, LocalDateTime dateTime);

}