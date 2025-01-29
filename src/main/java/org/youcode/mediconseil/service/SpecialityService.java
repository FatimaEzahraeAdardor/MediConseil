package org.youcode.mediconseil.service;
import org.springframework.data.domain.Page;
import org.youcode.mediconseil.domain.Speciality;
import java.util.Optional;
import java.util.UUID;


public interface SpecialityService {
    Speciality save(Speciality speciality);
    Speciality update(UUID id, Speciality speciality);
    Boolean delete(UUID id);
    Optional<Speciality> findById(UUID id);
    Page<Speciality> getAllSpecialitiesPaginated(int page, int size);

}
