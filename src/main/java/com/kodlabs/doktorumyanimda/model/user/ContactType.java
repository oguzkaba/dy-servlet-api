package com.kodlabs.doktorumyanimda.model.user;

import java.util.stream.Stream;

public enum ContactType {
    SMS("sms"),
    MAIL("mail");
    private String type;
    ContactType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }

    public static ContactType findContactType(String type){
        return Stream.of(values())
                .filter(v -> v.type.equals(type))
                .findFirst()
                .orElse(null);
    }
}
