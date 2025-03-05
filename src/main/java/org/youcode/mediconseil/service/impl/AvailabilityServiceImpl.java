package org.youcode.mediconseil.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.youcode.mediconseil.domain.Availability;
import org.youcode.mediconseil.repository.AvailabilityRepository;
import org.youcode.mediconseil.service.AvailabiityService;
import org.youcode.mediconseil.web.exception.ResourceNotFoundException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabiityService {
    private final AvailabilityRepository availabilityRepository;

    @Override
    public Availability save(Availability availability) {
        return availabilityRepository.save(availability);
    }

    @Override
    public Availability update(UUID id, Availability availability) {
        Availability existingAvailability = availabilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Availability not found"));

        existingAvailability.setStartTime(availability.getStartTime());
        existingAvailability.setEndTime(availability.getEndTime());
        existingAvailability.setIsBooked(availability.getIsBooked());

        return availabilityRepository.save(existingAvailability);
    }

    @Override
    public Boolean delete(UUID id) {
        availabilityRepository.deleteById(id);
        return true;
    }

    @Override
    public Optional<Availability> findById(UUID id) {
        return availabilityRepository.findById(id);
    }

    @Override
    public Page<Availability> getAllAvailabilitiesPaginated(int page, int size) {
        return availabilityRepository.findAll(PageRequest.of(page, size));
    }

    // New method to find available slots for a specific doctor
    public Page<Availability> findAvailableSlotsByDoctor(UUID doctorId, int page, int size) {
        return availabilityRepository.findByDoctorIdAndIsBookedFalse(doctorId, PageRequest.of(page, size));
    }
}