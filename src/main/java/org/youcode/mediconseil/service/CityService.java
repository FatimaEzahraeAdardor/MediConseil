package org.youcode.mediconseil.service;
import org.springframework.stereotype.Service;
import org.youcode.mediconseil.domain.City;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface CityService {
    City save(City city);
    City update(UUID id , City city);
    Boolean delete(UUID id);
    Optional<City> findById(UUID id);
    List<City> findAll();
    List<City> findAllByRegion(String region);

}
