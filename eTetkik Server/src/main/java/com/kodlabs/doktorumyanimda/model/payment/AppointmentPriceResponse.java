package com.kodlabs.doktorumyanimda.model.payment;

import java.math.BigDecimal;

public class AppointmentPriceResponse {
    private BigDecimal price;
    private boolean isFree;
    private String doctorID;
    private String patientID;

    public AppointmentPriceResponse() {
    }

    public AppointmentPriceResponse(BigDecimal price, boolean isFree, String doctorID, String patientID) {
        this.price = price;
        this.isFree = isFree;
        this.doctorID = doctorID;
        this.patientID = patientID;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }
}
