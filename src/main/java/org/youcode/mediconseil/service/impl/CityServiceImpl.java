package org.youcode.mediconseil.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.youcode.mediconseil.domain.City;
import org.youcode.mediconseil.repository.CityRepository;
import org.youcode.mediconseil.service.CityService;
import org.youcode.mediconseil.web.exception.AlreadyExistException;
import org.youcode.mediconseil.web.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    public CityServiceImpl(CityRepository cityRepository) {
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
        City foundCity = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("city not found"));
        if (cityRepository.existsByName(city.getName())) {
            throw new AlreadyExistException("this city already exist");
        }
        foundCity.setName(city.getName() != null ? city.getName() : foundCity.getName());
        return cityRepository.save(foundCity);
    }

    @Override
    public Boolean delete(UUID id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("city not found"));
        cityRepository.delete(city);
        return true;
    }

    @Override
    public Optional<City> findById(UUID id) {
        Optional<City> cityOptional = cityRepository.findById(id);
        if (cityOptional.isPresent()) {
            return cityOptional;
        } else {
            throw new ResourceNotFoundException("city not found");
        }
    }

    @Override
    public Page<City> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return cityRepository.findAll(pageable);
    }

    @Override
    public Page<City> findAllByRegion(String region, int page, int size) {
        if(region != null && !region.isEmpty()) {
            Pageable pageable = PageRequest.of(page, size);
            return cityRepository.findByRegion(region,pageable);
        }
        return null;
    }

    @Override
    public List<City> findAllCities() {
        return cityRepository.findAll();
    }

    @Override
    public List<String> getAllRegions() {
        return cityRepository.findDistinctRegions();
    }


}
