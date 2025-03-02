package org.youcode.mediconseil.service;
import org.springframework.data.domain.Page;
import org.youcode.mediconseil.domain.Consultation;

import java.util.Optional;
import java.util.UUID;

public interface ConsultaionService {
    Consultation save(Consultation consultation, UUID availabilityId);
    Consultation update(Consultation consultation, UUID availabilityId);
    public Consultation cancelConsultation(UUID consultationId);
    public Consultation confirmConsultation(UUID consultationId);
    Boolean delete(UUID id);
    Optional<Consultation> findByID(UUID id);
    Page<Consultation> getAllConsultationsPaginated(int page, int size);
    public Page<Consultation> getConsultationsByDoctorId(UUID doctorId, int page, int size);
}
