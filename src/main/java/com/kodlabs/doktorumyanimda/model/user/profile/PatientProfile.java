package com.kodlabs.doktorumyanimda.model.user.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PatientProfile extends  Profile implements IProfile{
    private String mail;
    private String phone;
    private int age;
    private boolean visiblePhone = true;

    public PatientProfile(String name, String surname, String gender, int age, String picture, String mail, String phone, boolean visiblePhone) {
        super(name, surname, gender, picture);
        this.mail = mail;
        this.age = age;
        this.phone = phone;
        this.visiblePhone = visiblePhone;
    }

    @JsonIgnore
    public boolean isValid(){
        return super.isValid();
    }
}
