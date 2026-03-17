package com.kodlabs.doktorumyanimda.model.patient.enabiz.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EnabizServiceInformation {
    private int id;
    private String name;
    private String serviceReferenceNo;
    private String sysTakipNo;
    private String updateDate;
    private String createDate;
    private boolean active;
}
