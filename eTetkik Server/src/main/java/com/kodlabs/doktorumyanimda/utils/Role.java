package com.kodlabs.doktorumyanimda.utils;

public enum Role{
    PATIENT((byte) 0),
    DOCTOR((byte) 1),
    ADMIN((byte) 2),

    FACILITY_ADMIN((byte) 3);
    private byte value;
    Role(byte value){
        this.value = value;
    }
    public byte value(){
        return this.value;
    }
}