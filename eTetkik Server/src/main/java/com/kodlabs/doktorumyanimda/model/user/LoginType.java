package com.kodlabs.doktorumyanimda.model.user;

import java.util.stream.Stream;

public enum LoginType {
    TC_NUMBER("tc_number"),
    PHONE("phone");
    private String type;
    LoginType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static LoginType getInstance(String type){
        return Stream.of(values())
                .filter(v -> v.type.equals(type))
                .findFirst()
                .orElse(null);
    }
}
