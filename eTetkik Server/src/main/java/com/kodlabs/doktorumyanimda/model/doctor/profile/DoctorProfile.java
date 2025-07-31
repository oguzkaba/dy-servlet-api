package com.kodlabs.doktorumyanimda.model.doctor.profile;

import com.kodlabs.doktorumyanimda.model.user.profile.Profile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DoctorProfile extends Profile {
    private String degree;

    public DoctorProfile(String name, String surname, String gender, String picture, String degree) {
        super(name, surname, gender, picture);
        this.degree = degree;
    }
}
