package org.youcode.mediconseil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.youcode.mediconseil.domain.Consultation;

import java.util.List;
import java.util.UUID;

    public interface ConsultationRepository extends JpaRepository<Consultation, UUID> {

}
