package org.youcode.mediconseil.web.vm.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestVM {
    @NotBlank(message = "Category name is required")
    private String name;
}