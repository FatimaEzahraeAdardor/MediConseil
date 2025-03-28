package org.youcode.mediconseil.web.vm.mapper;

import org.mapstruct.Mapper;
import org.youcode.mediconseil.domain.Category;
import org.youcode.mediconseil.web.vm.request.CategoryRequestVM;
import org.youcode.mediconseil.web.vm.response.CategoryResponseVM;

@Mapper(componentModel = "spring")

public interface CategoryMapper {
        CategoryResponseVM toVM(Category category);
        Category toEntity(CategoryRequestVM categoryRequestVM);
}
