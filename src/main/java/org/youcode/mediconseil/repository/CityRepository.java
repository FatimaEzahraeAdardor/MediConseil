package org.youcode.mediconseil.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.youcode.mediconseil.domain.City;

import java.util.List;
import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {
    boolean existsByName(String name);
    Page<City> findByRegion(String region , Pageable pageable);
    @Query("SELECT DISTINCT c.region FROM City c ORDER BY c.region ASC")
    List<String> findDistinctRegions();


}
