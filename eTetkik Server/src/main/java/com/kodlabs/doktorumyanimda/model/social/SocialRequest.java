package com.kodlabs.doktorumyanimda.model.social;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialRequest <T extends Social>{
    private String userID;
    private T social;

    public boolean isValid(){
        return !TextUtils.isEmpty(userID) && social != null && social.isValid();
    }
}
