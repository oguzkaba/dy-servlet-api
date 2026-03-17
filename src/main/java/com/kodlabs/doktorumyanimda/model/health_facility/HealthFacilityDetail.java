package com.kodlabs.doktorumyanimda.model.health_facility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HealthFacilityDetail {
    private String name;
    private String description;
    private String address;
    private Long tax_number;
    private String phone;
    private int facilityID;
    private String createDate;
}
