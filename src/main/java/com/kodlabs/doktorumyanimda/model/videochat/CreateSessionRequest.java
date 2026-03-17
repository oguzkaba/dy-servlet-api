package com.kodlabs.doktorumyanimda.model.videochat;


import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSessionRequest {
    private String userID;
    private int role;

    public boolean isValid(){
        return !TextUtils.isEmpty(userID);
    }
}
