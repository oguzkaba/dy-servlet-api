package com.kodlabs.doktorumyanimda.model.social;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Social {
    private String id;
    private int type;
    private String link;
    @JsonIgnore
    public boolean isValid(){
        return !TextUtils.isEmpty(link);
    }
}