package com.kodlabs.doktorumyanimda.model.doctor;

import com.kodlabs.doktorumyanimda.model.social.SocialAccount;
import com.kodlabs.doktorumyanimda.model.social.SocialShare;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DoctorPayInformation {
    private String fullName;
    private String doctorID;
    private String branch;
    private String education;
    private String introduceYourSelf;
    private String areaExperts;
    private String picture;
    private List<SocialAccount> socialAccounts;
    private List<SocialShare> socialShares;
    private List<DoctorPeakDetail> peakDetails;
    private String address = null;
    private String webAddress;
    private int dayHourStart;
    private int dayHourEnd;
    private int dayMinutePeriod;
    private BigDecimal appointmentPrice;
}
