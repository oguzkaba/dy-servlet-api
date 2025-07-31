package com.kodlabs.doktorumyanimda.model.user.profile;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProfileUpdateRequest <T extends IProfile> {
    private String userID;
    private T profile;

    public boolean isValid(){
        return !TextUtils.isEmpty(userID) && profile.isValid();
    }
}
