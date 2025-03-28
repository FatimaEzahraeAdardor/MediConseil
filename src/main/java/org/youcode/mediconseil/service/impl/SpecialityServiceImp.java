package org.youcode.mediconseil.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.youcode.mediconseil.domain.Speciality;
import org.youcode.mediconseil.repository.SpecialityRepository;
import org.youcode.mediconseil.service.SpecialityService;
import org.youcode.mediconseil.web.exception.AlreadyExistException;
import org.youcode.mediconseil.web.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class SpecialityServiceImp implements SpecialityService {
    private final SpecialityRepository specialityRepository;
    public SpecialityServiceImp(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }
    @Override
    public Speciality save(Speciality speciality) {
        if (speciality == null) {
            throw new ResourceNotFoundException("speciality cannot be null");
        }
        Optional<Speciality> existingSpeciality = specialityRepository.findByName(speciality.getName());
        if (existingSpeciality.isPresent()) {
            throw new AlreadyExistException("A category with the same name already exists");
        }
        return specialityRepository.save(speciality);
    }

    @Override
    public Speciality update(UUID id, Speciality speciality) {
        Speciality existingSpeciality = specialityRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Speciality not found"));
        if (specialityRepository.existsByName(speciality.getName())) {
            throw new AlreadyExistException("this speciality already exist");
        }
        existingSpeciality.setName(speciality.getName() != null ? speciality.getName() : existingSpeciality.getName());
        return specialityRepository.save(existingSpeciality);
    }


    @Override
    public Boolean delete(UUID id) {
        Speciality speciality = specialityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("speciality not found"));
        specialityRepository.delete(speciality);
        return true;
    }

    @Override
    public Optional<Speciality> findById(UUID id) {
        Optional<Speciality> specialityOptional = specialityRepository.findById(id);
        if (specialityOptional.isPresent()) {
            return specialityOptional;
        } else {
            throw new ResourceNotFoundException("speciality not found");
        }
    }

    @Override
    public Page<Speciality> getAllSpecialitiesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return specialityRepository.findAll(pageable);
    }

    @Override
    public List<Speciality> findAllSpecialtiesList() {
        return specialityRepository.findAll();
    }
}
