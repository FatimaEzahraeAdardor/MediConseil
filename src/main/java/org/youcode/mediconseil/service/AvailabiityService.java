package org.youcode.mediconseil.service;

import org.springframework.data.domain.Page;
import org.youcode.mediconseil.domain.Article;
import org.youcode.mediconseil.domain.Availability;

import java.util.Optional;
import java.util.UUID;

public interface AvailabiityService {
    Availability save(Availability availability);
    Availability update(UUID id,Availability availability);
    Boolean delete(UUID id);
    Optional<Availability> findById(UUID id);
    public Page<Availability> getAllAvailabilitiesPaginated(int page, int size) ;
        Page<Availability> findAvailableSlotsByDoctor(UUID doctorId, int page, int size) ;
}
