package org.youcode.mediconseil.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.youcode.mediconseil.domain.Availability;
import org.youcode.mediconseil.domain.Consultation;
import org.youcode.mediconseil.domain.User;
import org.youcode.mediconseil.domain.enums.ConsultationStatus;
import org.youcode.mediconseil.repository.ConsultationRepository;
import org.youcode.mediconseil.service.AvailabilityService;
import org.youcode.mediconseil.service.ConsultationService;
import org.youcode.mediconseil.service.UserService;
import org.youcode.mediconseil.web.exception.ResourceNotFoundException;
import org.youcode.mediconseil.web.vm.request.ConsultationRequestVm;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final AvailabilityService availabilityService;
    private final UserService userService;
    private final EmailService emailService;

    @Override
    public List<Consultation> findAll() {
        return consultationRepository.findAll();
    }

    @Override
    public Optional<Consultation> findById(UUID id) {
        return consultationRepository.findById(id);
    }

    @Override
    @Transactional
    public Consultation save(Consultation consultation) {
        return consultationRepository.save(consultation);
    }

    @Override
    @Transactional
    public Consultation bookConsultation(ConsultationRequestVm requestVm) {
        // First, find or create the availability
        Availability availability = availabilityService.findOrCreateAvailabilityById(
                requestVm.getDoctorId(),
                requestVm.getAvailabilityId(),
                requestVm.getDateConsultation(),
                requestVm.getDateConsultation().plusMinutes(30) // Assuming 30-minute consultations
        );

        // Check if the availability is already booked
        if (Boolean.TRUE.equals(availability.getIsBooked())) {
            throw new RuntimeException("Ce créneau horaire est déjà réservé.");
        }

        // Mark the availability as booked
        availability.setIsBooked(true);
        availabilityService.save(availability);

        // Get the patient
        User patient = userService.findByID(requestVm.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with ID: " + requestVm.getPatientId()));

        // Create and save the consultation
        Consultation consultation = new Consultation();
        consultation.setDoctor(availability.getDoctor());
        consultation.setPatient(patient);
        consultation.setMotif(requestVm.getMotif());
        consultation.setDateConsultation(requestVm.getDateConsultation());
        consultation.setStatus(ConsultationStatus.PENDING); // Set initial status

        return consultationRepository.save(consultation);
    }

    @Override
    public Consultation update(UUID id, Consultation consultation) {
        Consultation existingConsultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation not found"));

        // Update fields
        existingConsultation.setMotif(consultation.getMotif());
        existingConsultation.setStatus(consultation.getStatus());
        // Update other fields as needed

        return consultationRepository.save(existingConsultation);
    }

    @Override
    public Boolean delete(UUID id) {
        consultationRepository.deleteById(id);
        return true;
    }

    @Override
    public Page<Consultation> findByPatientId(UUID patientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return consultationRepository.findByPatientId(patientId, pageable);
    }

    @Override
    public Page<Consultation> findByDoctorId(UUID doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return consultationRepository.findByDoctorId(doctorId, pageable);
    }

    @Override
    @Transactional
    public Consultation cancelConsultation(UUID id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation not found with ID: " + id));

        if (!ConsultationStatus.PENDING.equals(consultation.getStatus())) {
            throw new RuntimeException("Seules les consultations en attente peuvent être annuléés.");
        }

        // Update consultation status
        consultation.setStatus(ConsultationStatus.CANCELLED);

        // Free up the time slot in availabilities if applicable
        Availability availability = availabilityService.findByDoctorAndStartDate(
                        consultation.getDoctor().getId(),
                        consultation.getDateConsultation())
                .orElse(null);

        if (availability != null) {
            availability.setIsBooked(false);
            availabilityService.save(availability);
        }

        return consultationRepository.save(consultation);
    }

    @Override
    @Transactional
    public Consultation confirmConsultation(UUID id) {
        Consultation consultation = consultationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation not found with ID: " + id));

        if (!ConsultationStatus.PENDING.equals(consultation.getStatus())) {
            throw new RuntimeException("Seules les consultations en attente peuvent être confirmées.");
        }


        consultation.setStatus(ConsultationStatus.CONFIRMED);

        Consultation confirmedConsultation = consultationRepository.save(consultation);

        emailService.sendConsultationConfirmationEmail(confirmedConsultation);
        return confirmedConsultation;
    }


}