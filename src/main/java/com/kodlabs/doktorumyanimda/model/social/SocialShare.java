package com.kodlabs.doktorumyanimda.model.social;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SocialShare extends Social{
    private String title;

    public SocialShare(String id, int type, String link, String title) {
        super(id, type, link);
        this.title = title;
    }

    public boolean isValid(){
        return super.isValid() && getType() == SocialType.OTHER.ordinal();
    }
}
