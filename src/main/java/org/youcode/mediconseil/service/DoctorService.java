package org.youcode.mediconseil.service;

import org.springframework.data.domain.Page;
import org.youcode.mediconseil.domain.Doctor;
import org.youcode.mediconseil.web.vm.request.DoctorRequestVm;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorService {
    Doctor save(Doctor doctor);
    Doctor update(UUID doctorId, DoctorRequestVm doctor);
    Boolean delete(UUID id);
    Optional<Doctor> findById(UUID id);
    Page<Doctor> getAllDoctorsPaginated(int page, int size);
    Page<Doctor> getDoctorsBySpecialty(UUID categoryId, int page, int size);
    List<Doctor> getDoctors();


}
