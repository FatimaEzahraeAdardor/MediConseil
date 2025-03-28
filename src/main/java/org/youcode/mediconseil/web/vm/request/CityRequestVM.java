package org.youcode.mediconseil.web.vm.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.youcode.mediconseil.domain.City;

@Getter
@Setter
public class CityRequestVM {
    @NotBlank(message = "name cannot be blank")
    private String name;

    @NotBlank(message = "region cannot be blank")
    private String region;
}