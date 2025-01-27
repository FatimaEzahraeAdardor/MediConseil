package org.youcode.mediconseil.web.api.category;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.youcode.mediconseil.domain.Category;
import org.youcode.mediconseil.service.CategoryService;
import org.youcode.mediconseil.web.vm.mapper.CategoryMapper;
import org.youcode.mediconseil.web.vm.request.CategoryRequestVM;
import org.youcode.mediconseil.web.vm.response.CategoryResponseVM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }


    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> addSpecies(@RequestBody @Valid CategoryRequestVM categoryRequestVM){
        Category category = categoryMapper.toEntity(categoryRequestVM);
        Category createdCategory = categoryService.save(category);
        CategoryResponseVM categoryResponseVm = categoryMapper.toVM(createdCategory);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "category created successfully");
        response.put("category", categoryResponseVm);
        return new  ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PutMapping("update/{id}")
    public ResponseEntity<Map<String, Object>> updateSpecies(@PathVariable UUID id, @RequestBody @Valid CategoryRequestVM categoryRequestVM) {
        Category category = categoryMapper.toEntity(categoryRequestVM);
        Category updatedCategory = categoryService.update(id,category);
        CategoryResponseVM categoryResponseVM = categoryMapper.toVM(updatedCategory);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "category updated successfully");
        response.put("category", categoryResponseVM);
        return new  ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSpecies(@PathVariable UUID id) {
      categoryService.delete(id);
     return ResponseEntity.ok("category deleted successfully");
    }
}
