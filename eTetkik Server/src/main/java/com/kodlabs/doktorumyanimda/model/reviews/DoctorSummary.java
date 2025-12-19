package com.kodlabs.doktorumyanimda.model.reviews;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DoctorSummary {
    private String doctorID;
    private Double avgRating;
    private Integer ratingCount;
    private Date lastReviewAt;

    public DoctorSummary(String doctorID, Double avgRating, Integer ratingCount, Date lastReviewAt) {
        this.doctorID = doctorID;
        this.avgRating = avgRating;
        this.ratingCount = ratingCount;
        this.lastReviewAt = lastReviewAt;
    }
}
