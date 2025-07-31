package com.kodlabs.doktorumyanimda.model.videochat;

public class UpdateTokenRequest {

    private String token;
    private String phone;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
