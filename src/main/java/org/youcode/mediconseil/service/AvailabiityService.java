package org.youcode.mediconseil.service;

import org.springframework.data.domain.Page;
import org.youcode.mediconseil.domain.Article;
import org.youcode.mediconseil.domain.Availability;

import java.util.Optional;
import java.util.UUID;

public interface AvailabiityService {
    Availability save(Availability availability);
    Availability update(Availability availability);
    Boolean delete(UUID id);
    Optional<Availability> findById(UUID id);
    Page<Availability> getAllAvailabilitsPaginated(int page, int size);

}
