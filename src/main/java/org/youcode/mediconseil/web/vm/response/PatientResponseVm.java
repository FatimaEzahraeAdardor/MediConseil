package org.youcode.mediconseil.web.vm.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponseVm {
    private UUID id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private CityResponseVM city;
    private String phoneNumber;
    private String image;
}
