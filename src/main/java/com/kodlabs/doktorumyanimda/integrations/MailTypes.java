package com.kodlabs.doktorumyanimda.integrations;

import java.util.stream.Stream;

public enum MailTypes {
    SUPPORT("support"),
    VERIFY("verify");
    private String type;
    MailTypes(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public static MailTypes findType(String type){
        return Stream.of(values())
                .filter(v -> v.type.equals(type))
                .findFirst()
                .orElse(null);
    }
}
