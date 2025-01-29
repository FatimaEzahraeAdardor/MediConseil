package org.youcode.mediconseil.web.vm.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.youcode.mediconseil.domain.City;
import org.youcode.mediconseil.domain.enums.Role;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank(message = "Username is required.")
    private String userName;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters.")
    private String password;

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @NotBlank(message = "Email is required.")
    private String email;

    @NotNull(message = "City is required.")
    private UUID city_id;

    @NotBlank(message = "Phone number is required.")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters.")
    private String phoneNumber;

    @NotNull(message = "Role is required.")
    private Role role;
}