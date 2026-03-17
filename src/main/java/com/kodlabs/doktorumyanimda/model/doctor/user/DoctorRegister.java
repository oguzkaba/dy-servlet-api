package com.kodlabs.doktorumyanimda.model.doctor.user;

import com.kodlabs.doktorumyanimda.model.doctor.DoctorBase;
import com.kodlabs.doktorumyanimda.model.doctor.detail.DoctorDetail;
import com.kodlabs.doktorumyanimda.model.doctor.profile.DoctorProfile;
import com.kodlabs.doktorumyanimda.model.doctor.recipe.DoctorRecipeInformation;
import com.kodlabs.doktorumyanimda.model.user.address.Address;
import com.kodlabs.doktorumyanimda.model.user.contact.Contact;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorRegister {
    private String adminID;
    private String doctorCode;
    private String password;
    private String tcNumber;
    private String serialNumber;
    private String registrationNumber;
    private Address address;
    private DoctorProfile profile;
    private DoctorDetail detail;
    private DoctorBase base;
    private Contact contact;
    private DoctorRecipeInformation recipeInformation;

    public boolean isValid(){
        return !TextUtils.isEmpty(adminID) && !TextUtils.isEmpty(tcNumber) && !TextUtils.isEmpty(password) && password.length() >= 6 && !TextUtils.isEmpty(doctorCode) && address != null && address.isValid() && profile != null && profile.isValid() &&
                detail != null && detail.isValid() && base != null && base.isValid() && contact != null && contact.isValid() && recipeInformation != null && recipeInformation.isValid();
    }
}
