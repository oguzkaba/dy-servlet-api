package com.kodlabs.doktorumyanimda.model.doctor;

import com.kodlabs.doktorumyanimda.utils.AdminRole;
import com.kodlabs.doktorumyanimda.utils.Role;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorUpdateSideAdminRequest {
    private String userID;
    private String type;
    private DoctorBaseProfile profile;

    public boolean isValid(){
        Byte role = AdminRole.getUserRole(type);
        return !TextUtils.isEmpty(userID) && role != null && (role == Role.ADMIN.value() || role == Role.FACILITY_ADMIN.value()) && profile != null && profile.isValid();
    }
}
