package org.youcode.mediconseil.service;

import org.springframework.data.domain.Page;
import org.youcode.mediconseil.domain.Category;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface CategoryService {
    Category save(Category category);
    Category update(UUID id,Category category);
    Boolean delete(UUID id);
    Optional<Category> findById(UUID id);
    Page<Category> getAllCategoriesPaginated(int page, int size);
    List<Category> findAllCategories();

}
