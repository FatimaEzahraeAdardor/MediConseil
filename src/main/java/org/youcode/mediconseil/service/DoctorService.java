package org.youcode.mediconseil.service;

import org.springframework.data.domain.Page;
import org.youcode.mediconseil.domain.Category;
import org.youcode.mediconseil.domain.Doctor;

import java.util.Optional;
import java.util.UUID;

public interface DoctorService {
    Doctor save(Doctor doctor);
    Doctor update(Doctor doctor);
    Boolean delete(UUID id);
    Optional<Doctor> findById(UUID id);
    Page<Doctor> getAllDoctorsPaginated(int page, int size);
    Page<Doctor> getDoctorsByCategory(Category category, int page, int size);


}
