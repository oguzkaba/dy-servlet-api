package com.kodlabs.doktorumyanimda.model.doctor.registration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationInformation {
    private String name;
    private String surname;
    private String birdDate;

}
