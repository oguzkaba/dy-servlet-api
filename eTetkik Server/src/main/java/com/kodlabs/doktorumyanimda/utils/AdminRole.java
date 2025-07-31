package com.kodlabs.doktorumyanimda.utils;

import java.util.stream.Stream;

public enum AdminRole {
    WEB_ADMIN("web", Role.ADMIN.value()),
    FACILITY_ADMIN("facility", Role.FACILITY_ADMIN.value());
    private String value;
    private byte userRole;

    AdminRole(String value, byte userRole){
        this.value = value;
        this.userRole = userRole;
    }
    public String getValue() {
        return value;
    }
    public static Byte getUserRole(String role){
        return Stream.of(values())
                .filter(v -> v.value.equals(role))
                .findFirst()
                .map(v -> v.userRole)
                .orElse(null);
    }

    public static AdminRole getAdminRole(String value){
        return Stream.of(values())
                .filter(v -> v.value.equals(value))
                .findFirst()
                .orElse(null);
    }
}
