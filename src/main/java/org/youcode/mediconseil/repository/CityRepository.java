package org.youcode.mediconseil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.youcode.mediconseil.domain.City;

import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {
}
