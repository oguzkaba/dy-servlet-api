package com.kodlabs.doktorumyanimda.model.reviews;

import com.kodlabs.doktorumyanimda.utils.TextUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReviewCreateRequest {
    private String patientID;
    private String doctorID;
    private Long appointmentID;
    private Integer rating;
    private String title;
    private String body;

    public boolean isValid() {
        return !TextUtils.isEmpty(patientID)
                && !TextUtils.isEmpty(doctorID)
                && appointmentID != null
                && rating != null && rating >= 1 && rating <= 5
                && !TextUtils.isEmpty(title)
                && !TextUtils.isEmpty(body);
    }
}
