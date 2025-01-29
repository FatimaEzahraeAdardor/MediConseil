package org.youcode.mediconseil.web.vm.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialityResponseVm {
    @NotBlank(message = "name cannot be blank")
    private String name;
}
