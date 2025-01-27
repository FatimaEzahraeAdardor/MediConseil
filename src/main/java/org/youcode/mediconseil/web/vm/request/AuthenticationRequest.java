package org.youcode.mediconseil.web.vm.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequest {
    @NotNull
    private String email;
    @NotNull
    private String password;
}
