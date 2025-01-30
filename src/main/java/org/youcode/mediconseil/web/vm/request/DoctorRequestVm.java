package org.youcode.mediconseil.web.vm.request;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DoctorRequestVm {

    @NotBlank(message = "Address cannot be blank")
    @Size(min = 5, max = 255, message = "Address must be between 5 and 255 characters")
    private String address;

    @NotBlank(message = "Experience field cannot be blank")
    @Size(min = 10, max = 500, message = "Experience must be between 10 and 500 characters")
    private String experiences;

    @NotBlank(message = "Diploma field cannot be blank")
    @Size(min = 3, max = 255, message = "Diploma must be between 3 and 255 characters")
    private String diploma;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Specialty ID cannot be null")
    @Positive(message = "Specialty ID must be a positive number")
    private UUID specialtyId;
}
