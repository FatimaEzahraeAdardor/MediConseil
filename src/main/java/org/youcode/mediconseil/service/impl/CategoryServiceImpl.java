package org.youcode.mediconseil.service.impl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.youcode.mediconseil.domain.Category;
import org.youcode.mediconseil.domain.Speciality;
import org.youcode.mediconseil.repository.CategoryRepository;
import org.youcode.mediconseil.service.CategoryService;
import org.youcode.mediconseil.web.exception.AlreadyExistException;
import org.youcode.mediconseil.web.exception.InvalidObjectExeption;

import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    @Override
    public Category save(Category category) {
        if (category == null) {
            throw new InvalidObjectExeption("Category cannot be null");
        }
        Optional<Category> existingCategory = categoryRepository.findByName(category.getName());
        if (existingCategory.isPresent()) {
            throw new AlreadyExistException("A category with the same name already exists");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category update(UUID id, Category category) {
        if (category == null || id == null) {
            throw new InvalidObjectExeption("Category and ID must not be null");
        }
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new InvalidObjectExeption("Category not found with id: " + id));
        Optional<Category> duplicateCategory = categoryRepository.findByName(category.getName());
        if (duplicateCategory.isPresent() && !duplicateCategory.get().getId().equals(id)) {
            throw new AlreadyExistException("A category with the same name already exists");
        }
        existingCategory.setName(category.getName() != null ? category.getName() : existingCategory.getName());
        return categoryRepository.save(existingCategory);
    }
    @Override
    public Boolean delete(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new InvalidObjectExeption("category not found"));
        categoryRepository.delete(category);
        return true;
    }

    @Override
    public Optional<Category> findById(UUID id) {
        Optional<Category> categoryOptional =  categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            return categoryOptional;
        }else {
            throw new InvalidObjectExeption("category not found");
        }

    }

    @Override
    public Page<Speciality> getAllCategoriesPaginated(int page, int size) {
        return null;
    }

}