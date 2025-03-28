package org.youcode.mediconseil.web.vm.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialityRequest {
    @NotBlank(message = "name cannot be blank")
    private String name;
}
