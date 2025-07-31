package com.kodlabs.doktorumyanimda.model.doctor;

import java.util.stream.Stream;

public enum DoctorType {
    GOVERNMENTAL(0),
    PRIVATE(1);
    private int type;
    DoctorType(int type){
        this.type = type;
    }
    public static boolean isAvailable(int type){
        return Stream.of(values())
                .anyMatch(v -> v.type == type);
    }
}
