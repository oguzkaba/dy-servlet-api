package com.kodlabs.doktorumyanimda.model.doctor;

import java.math.BigDecimal;

public class DoctorPriceUpdateRequest {
    private BigDecimal price;

    public DoctorPriceUpdateRequest() {
    }

    public DoctorPriceUpdateRequest(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isValid() {
        return price != null && price.compareTo(BigDecimal.ZERO) >= 0;
    }
}
