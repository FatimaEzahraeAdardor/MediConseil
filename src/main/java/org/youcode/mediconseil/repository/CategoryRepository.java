package org.youcode.mediconseil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.youcode.mediconseil.domain.Category;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findByName(String name);
}
