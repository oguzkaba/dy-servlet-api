package com.kodlabs.doktorumyanimda.model.social;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SocialAccount extends Social{

    public SocialAccount(String id, int type, String link) {
        super(id, type, link);
    }

    @JsonIgnore
    public boolean isValid(){
        return super.isValid() && getType() != SocialType.OTHER.ordinal();
    }
}
