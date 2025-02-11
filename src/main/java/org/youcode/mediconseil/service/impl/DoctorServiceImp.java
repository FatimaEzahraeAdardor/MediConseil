package org.youcode.mediconseil.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.youcode.mediconseil.domain.Category;
import org.youcode.mediconseil.domain.City;
import org.youcode.mediconseil.domain.Doctor;
import org.youcode.mediconseil.repository.DoctorRepository;
import org.youcode.mediconseil.service.DoctorService;
import org.youcode.mediconseil.web.exception.InvalidObjectExeption;
import org.youcode.mediconseil.web.vm.request.DoctorRequestVm;

import java.util.Optional;
import java.util.UUID;
@Service
public class DoctorServiceImp implements DoctorService {
    private final DoctorRepository doctorRepository;
    public DoctorServiceImp(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }
    @Override
    public Doctor save(Doctor doctor) {
        if (doctor== null) {
            throw new InvalidObjectExeption("Doctor cannot be null");
        }
        return doctorRepository.save(doctor);

    }

    @Override
    public Doctor update(Doctor doctor) {
        return null;
    }

    @Override
    public Boolean delete(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new InvalidObjectExeption("doctor not found"));
        doctorRepository.delete(doctor);
        return true;
    }

    @Override
    public Optional<Doctor> findById(UUID id) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if (doctorOptional.isPresent()) {
            return doctorOptional;
        } else {
            throw new InvalidObjectExeption("city not found");
        }
    }

    @Override
    public Page<Doctor> getAllDoctorsPaginated(int page, int size) {
        return null;
    }

    @Override
    public Page<Doctor> getDoctorsByCategory(Category category, int page, int size) {
        return null;
    }
}
