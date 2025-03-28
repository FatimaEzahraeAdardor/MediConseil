package org.youcode.mediconseil.service;

import org.springframework.data.domain.Page;
import org.youcode.mediconseil.domain.Availability;
import org.youcode.mediconseil.domain.Doctor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvailabilityService {
    Availability save(Availability availability);

    Availability update(UUID id, Availability availability);

    Boolean delete(UUID id);

    Optional<Availability> findById(UUID id);
    Optional<Availability> findByDoctorAndStartDate(UUID doctorId, LocalDateTime startTime);

    Page<Availability> getAllAvailabilitiesPaginated(int page, int size);

    Page<Availability> findAvailableSlotsByDoctor(UUID doctorId, int page, int size);

    List<Availability> getAvailabilitiesByDoctor(UUID doctorId);

    List<Availability> getAvailabilitiesByDoctorAndDate(UUID doctorId, LocalDate date);

    List<Availability> getOrGenerateAvailabilitiesByDoctorAndDate(UUID doctorId, LocalDate date);

    Availability findOrCreateAvailabilityById(UUID doctorId, UUID availabilityId, LocalDateTime startTime, LocalDateTime endTime);
}