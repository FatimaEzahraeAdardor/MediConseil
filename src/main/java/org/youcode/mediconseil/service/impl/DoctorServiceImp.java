package org.youcode.mediconseil.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.youcode.mediconseil.domain.*;
import org.youcode.mediconseil.repository.CategoryRepository;
import org.youcode.mediconseil.repository.DoctorRepository;
import org.youcode.mediconseil.service.CityService;
import org.youcode.mediconseil.service.DoctorService;
import org.youcode.mediconseil.service.SpecialityService;
import org.youcode.mediconseil.web.exception.ResourceNotFoundException;
import org.youcode.mediconseil.web.vm.request.DoctorRequestVm;

import java.util.Optional;
import java.util.UUID;
@Service
public class DoctorServiceImp implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final CityService cityService;
    private final SpecialityService specialityService;

    public DoctorServiceImp(DoctorRepository doctorRepository, CityService cityService, SpecialityService specialityService) {
        this.doctorRepository = doctorRepository;
        this.cityService = cityService;
        this.specialityService = specialityService;
    }

    @Override
    public Doctor save(Doctor doctor) {
        if (doctor == null) {
            throw new ResourceNotFoundException("Doctor cannot be null");
        }
        return doctorRepository.save(doctor);

    }

    @Override
    public Doctor update(UUID doctorId, DoctorRequestVm doctor) {

        // Fetch existing doctor
        Doctor existingDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + doctorId));

        // Update city if provided and not null
        if (doctor.getCityId() != null) {
            City city = cityService.findById(doctor.getCityId())
                    .orElseThrow(() -> new IllegalArgumentException("City not found with ID: " + doctor.getCityId()));
            existingDoctor.setCity(city);
        }
        if (doctor.getSpecialtyId() != null) {
            Speciality speciality = specialityService.findById(doctor.getSpecialtyId())
                    .orElseThrow(() -> new IllegalArgumentException("specialty not found with ID: " + doctor.getSpecialtyId()));
            existingDoctor.setSpeciality(speciality);
        }


        // Update general user details
        existingDoctor.setUserName(doctor.getUserName() != null ? doctor.getUserName() : existingDoctor.getUsername());
        existingDoctor.setFirstName(doctor.getFirstName() != null ? doctor.getFirstName() : existingDoctor.getFirstName());
        existingDoctor.setLastName(doctor.getLastName() != null ? doctor.getLastName() : existingDoctor.getLastName());
        existingDoctor.setEmail(doctor.getEmail() != null ? doctor.getEmail() : existingDoctor.getEmail());
        existingDoctor.setPhoneNumber(doctor.getPhoneNumber() != null ? doctor.getPhoneNumber() : existingDoctor.getPhoneNumber());
        existingDoctor.setPrice(doctor.getPrice() != null ? doctor.getPrice() : existingDoctor.getPrice());
        existingDoctor.setAddress(doctor.getAddress() != null ? doctor.getAddress() : existingDoctor.getAddress());
        existingDoctor.setDescription(doctor.getDescription() != null ? doctor.getDescription() : existingDoctor.getDescription());
        existingDoctor.setExperiences(doctor.getExperiences() != null ? doctor.getExperiences() : existingDoctor.getExperiences());
        existingDoctor.setDiploma(doctor.getDiploma() != null ? doctor.getDiploma() : existingDoctor.getDiploma());
        if (doctor.getPassword() != null) {
            existingDoctor.setPassword(BCrypt.hashpw(doctor.getPassword(), BCrypt.gensalt()));
        }

        // Save updated user
        existingDoctor = doctorRepository.save(existingDoctor);
        return existingDoctor;
    }

    @Override
    public Boolean delete(UUID id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("doctor not found"));
        doctorRepository.delete(doctor);
        return true;
    }

    @Override
    public Optional<Doctor> findById(UUID id) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if (doctorOptional.isPresent()) {
            return doctorOptional;
        } else {
            throw new ResourceNotFoundException("city not found");
        }
    }

    @Override
    public Page<Doctor> getAllDoctorsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return doctorRepository.findAll(pageable);
    }

    @Override
    public Page<Doctor> getDoctorsBySpecialty(UUID specialtyId, int page, int size) {
        if (specialtyId != null) {
            Speciality speciality = specialityService.findById(specialtyId)
                    .orElseThrow(() -> new IllegalArgumentException("specialty not found with ID: "));
        }
        Pageable pageable = PageRequest.of(page, size);
        return doctorRepository.findBySpecialityId(specialtyId,pageable);
    }
}
