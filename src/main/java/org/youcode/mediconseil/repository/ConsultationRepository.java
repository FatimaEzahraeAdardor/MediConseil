package org.youcode.mediconseil.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.youcode.mediconseil.domain.Consultation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

    public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {
        Page<Consultation> getConsultationsByDoctorId(UUID doctorId, Pageable pageable);
        Optional<Consultation> findByIdAndDoctorId(UUID consultationId, UUID docId);

}
