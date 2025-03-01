package org.youcode.mediconseil.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.youcode.mediconseil.domain.Availability;
import org.youcode.mediconseil.domain.Consultation;
import org.youcode.mediconseil.domain.enums.ConsultationStatus;
import org.youcode.mediconseil.repository.AvailabilityRepository;
import org.youcode.mediconseil.repository.ConsultationRepository;
import org.youcode.mediconseil.service.ConsultaionService;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultaionService {
    private final ConsultationRepository consultationRepository;
    private final AvailabilityRepository availabilityRepository;

    @Override
    @Transactional
    public Consultation save(Consultation consultation, UUID availabilityId) {
        // Find the availability
        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        // Check if availability is already booked
        if (Boolean.TRUE.equals(availability.getIsBooked())) {
            throw new RuntimeException("This time slot is already booked");
        }

        // Verify doctor matches the availability
        if (!availability.getDoctor().getId().equals(consultation.getDoctor().getId())) {
            throw new RuntimeException("Doctor does not match the availability");
        }

        // Explicitly set the consultation date to the availability start time
        consultation.setDateConsultation(availability.getStartTime());

        // Additional validation if needed
        if (!consultation.getDateConsultation().equals(availability.getStartTime())) {
            throw new RuntimeException("Consultation date must match availability start time");
        }

        // Mark availability as booked
        availability.setIsBooked(true);
        availabilityRepository.save(availability);

        // Ensure status is set to PENDING
        consultation.setStatus(ConsultationStatus.PENDING);

        // Save consultation
        return consultationRepository.save(consultation);
    }

    @Override
    @Transactional
    public Consultation update(Consultation consultation, UUID availabilityId) {
        // Find existing consultation
        Consultation existingConsultation = consultationRepository.findById(consultation.getId())
                .orElseThrow(() -> new RuntimeException("Consultation not found"));

        // Check if consultation is in PENDING status
        if (existingConsultation.getStatus() != ConsultationStatus.PENDING) {
            throw new RuntimeException("Only PENDING consultations can be updated");
        }

        // Find the new availability
        Availability newAvailability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Availability not found"));

        // Check if new availability is already booked
        if (Boolean.TRUE.equals(newAvailability.getIsBooked())) {
            throw new RuntimeException("This time slot is already booked");
        }

        // Verify doctor matches the new availability
        if (!newAvailability.getDoctor().getId().equals(consultation.getDoctor().getId())) {
            throw new RuntimeException("Doctor does not match the new availability");
        }

        // Update consultation details
        existingConsultation.setDateConsultation(newAvailability.getStartTime());
        existingConsultation.setMotif(consultation.getMotif());
        existingConsultation.setDoctor(newAvailability.getDoctor());

        // Only update patient if provided
        if (consultation.getPatient() != null) {
            existingConsultation.setPatient(consultation.getPatient());
        }

        // Mark new availability as booked
        newAvailability.setIsBooked(true);
        availabilityRepository.save(newAvailability);

        // Save and return updated consultation
        return consultationRepository.save(existingConsultation);
    }    @Override
    public Boolean delete(UUID id) {
        consultationRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<Consultation> findByID(UUID id) {
        return consultationRepository.findById(id);
    }

    @Override
    public Page<Consultation> getAllConsultationsPaginated(int page, int size) {
        return consultationRepository.findAll(PageRequest.of(page, size));
    }
}