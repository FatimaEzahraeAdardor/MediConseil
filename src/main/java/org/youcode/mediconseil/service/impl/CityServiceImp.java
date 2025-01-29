package org.youcode.mediconseil.service.impl;

import org.springframework.stereotype.Service;
import org.youcode.mediconseil.domain.City;
import org.youcode.mediconseil.repository.CityRepository;
import org.youcode.mediconseil.service.CityService;
import org.youcode.mediconseil.web.exception.AlreadyExistException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class CityServiceImp implements CityService {
    private final CityRepository cityRepository;

    public CityServiceImp(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public City save(City city) {
        if (cityRepository.existsByName(city.getName())) {
            throw new AlreadyExistException("this city already exist");
        }
        return cityRepository.save(city);
    }

    @Override
    public City update(UUID id, City city) {
        return null;
    }

    @Override
    public Boolean delete(UUID id) {
        return null;
    }

    @Override
    public Optional<City> findById(UUID id) {
        return cityRepository.findById(id);
    }

    @Override
    public List<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public List<City> findByRegion(String region) {
        return List.of();
    }

}
