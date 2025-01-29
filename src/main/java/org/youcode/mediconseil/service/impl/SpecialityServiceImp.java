package org.youcode.mediconseil.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.youcode.mediconseil.domain.Category;
import org.youcode.mediconseil.domain.Speciality;
import org.youcode.mediconseil.repository.SpecialityRepository;
import org.youcode.mediconseil.service.SpecialityService;
import org.youcode.mediconseil.web.exception.AlreadyExistException;
import org.youcode.mediconseil.web.exception.InvalidObjectExeption;

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
            throw new InvalidObjectExeption("speciality cannot be null");
        }
        Optional<Speciality> existingSpeciality = specialityRepository.findByName(speciality.getName());
        if (existingSpeciality.isPresent()) {
            throw new AlreadyExistException("A category with the same name already exists");
        }
        return specialityRepository.save(speciality);
    }

    @Override
    public Speciality update(UUID id, Speciality speciality) {
        return null;
    }

    @Override
    public Boolean delete(UUID id) {
        return null;
    }

    @Override
    public Optional<Speciality> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Page<Speciality> getAllSpecialitiesPaginated(int page, int size) {
        return null;
    }
}
