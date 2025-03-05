package org.youcode.mediconseil.web.vm.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponseVm {
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private CityResponseVM city;
    private String phoneNumber;
    private String address;
    private String experiences;
    private String diploma;
    private String description;
    private Double price;
    private SpecialityResponseVm specialty;
}
