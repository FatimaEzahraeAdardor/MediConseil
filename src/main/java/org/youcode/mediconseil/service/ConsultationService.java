package org.youcode.mediconseil.service;

import org.springframework.data.domain.Page;
import org.youcode.mediconseil.domain.Consultation;
import org.youcode.mediconseil.web.vm.request.ConsultationRequestVm;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsultationService {
    List<Consultation> findAll();

    Optional<Consultation> findById(UUID id);

    Consultation save(Consultation consultation);

    Consultation bookConsultation(ConsultationRequestVm requestVm);

    Consultation update(UUID id, Consultation consultation);

    Boolean delete(UUID id);

    Page<Consultation> findByPatientId(UUID patientId , int page, int size);

    Page<Consultation> findByDoctorId(UUID doctorId, int page, int size);
    Consultation cancelConsultation(UUID id);
    Consultation confirmConsultation(UUID id);
}