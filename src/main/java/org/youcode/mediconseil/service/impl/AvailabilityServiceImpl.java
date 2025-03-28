package org.youcode.mediconseil.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.youcode.mediconseil.domain.Availability;
import org.youcode.mediconseil.domain.Doctor;
import org.youcode.mediconseil.repository.AvailabilityRepository;
import org.youcode.mediconseil.repository.DoctorRepository;
import org.youcode.mediconseil.service.AvailabilityService;
import org.youcode.mediconseil.web.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {
    private final AvailabilityRepository availabilityRepository;
    private final DoctorRepository doctorRepository;

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
    public Optional<Availability> findByDoctorAndStartDate(UUID doctorId, LocalDateTime startTime) {
        return availabilityRepository.findByDoctorIdAndStartTime(doctorId, startTime);
    }

    @Override
    public Page<Availability> getAllAvailabilitiesPaginated(int page, int size) {
        return availabilityRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Page<Availability> findAvailableSlotsByDoctor(UUID doctorId, int page, int size) {
        return availabilityRepository.findByDoctorIdAndIsBookedFalse(doctorId, PageRequest.of(page, size));
    }

    @Override
    public List<Availability> getAvailabilitiesByDoctor(UUID doctorId) {
        return availabilityRepository.findByDoctorIdAndIsBookedFalseOrderByStartTimeAsc(doctorId);
    }

    @Override
    public List<Availability> getAvailabilitiesByDoctorAndDate(UUID doctorId, LocalDate date) {
        return availabilityRepository.findAvailableSlotsByDoctorAndDate(doctorId, date);
    }

    @Override
    public List<Availability> getOrGenerateAvailabilitiesByDoctorAndDate(UUID doctorId, LocalDate date) {
        // Ensure we never generate availabilities for today or past days
        LocalDate today = LocalDate.now();
        if (date.isBefore(today) || date.isEqual(today)) {
            date = today.plusDays(1); // Use tomorrow instead
        }

        // First check if we have stored availabilities for this date
        List<Availability> storedAvailabilities = availabilityRepository.findAvailableSlotsByDoctorAndDate(doctorId, date);

        // If we have stored availabilities, return them
        if (!storedAvailabilities.isEmpty()) {
            return storedAvailabilities;
        }

        // Otherwise, generate new availabilities
        return generateAvailabilitiesForDay(doctorId, date);
    }

    @Override
    @Transactional
    public Availability findOrCreateAvailabilityById(UUID doctorId, UUID availabilityId, LocalDateTime startTime, LocalDateTime endTime) {
        // First try to find the availability in the database
        Optional<Availability> existingAvailability = availabilityRepository.findById(availabilityId);

        if (existingAvailability.isPresent()) {
            return existingAvailability.get();
        }

        // If not found, create and persist a new one
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));

        Availability newAvailability = new Availability();
        newAvailability.setId(availabilityId); // Use the same ID that was sent from the frontend
        newAvailability.setStartTime(startTime);
        newAvailability.setEndTime(endTime);
        newAvailability.setIsBooked(false);
        newAvailability.setDoctor(doctor);

        return availabilityRepository.save(newAvailability);
    }

    private List<Availability> generateAvailabilitiesForDay(UUID doctorId, LocalDate date) {
        // Check if the provided date is today or in the past
        LocalDate today = LocalDate.now();
        if (date.isBefore(today) || date.isEqual(today)) {
            // If date is today or in the past, use tomorrow
            date = today.plusDays(1);
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + doctorId));

        // Generate time slots
        List<Availability> availabilities = new ArrayList<>();

        // Consultation hours, e.g. from 9am to 5pm
        LocalTime startHour = LocalTime.of(9, 0);
        LocalTime endHour = LocalTime.of(17, 0);

        // Duration of a consultation (in minutes)
        int consultationDuration = 30;

        // Create slots for the day
        LocalTime currentTime = startHour;
        while (currentTime.plusMinutes(consultationDuration).isBefore(endHour) ||
                currentTime.plusMinutes(consultationDuration).equals(endHour)) {

            LocalDateTime slotStart = LocalDateTime.of(date, currentTime);
            LocalDateTime slotEnd = slotStart.plusMinutes(consultationDuration);

            // Create an Availability object without persisting it
            Availability availability = new Availability();
            availability.setId(UUID.randomUUID());
            availability.setStartTime(slotStart);
            availability.setEndTime(slotEnd);
            availability.setIsBooked(false);
            availability.setDoctor(doctor);

            availabilities.add(availability);

            // Move to the next slot
            currentTime = currentTime.plusMinutes(consultationDuration);
        }

        return availabilities;
    }}