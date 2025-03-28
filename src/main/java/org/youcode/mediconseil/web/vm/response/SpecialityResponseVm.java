package org.youcode.mediconseil.web.vm.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SpecialityResponseVm {
    private UUID id;
    private String name;
}
