package com.kodlabs.doktorumyanimda.model.health_facility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthFacility {
    private int id;
    private String name;
    private String description;
    private String address;
    private String phone;
    private int facilityID;
    private String createDate;
}
