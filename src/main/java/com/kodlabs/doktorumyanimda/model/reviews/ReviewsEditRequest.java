package com.kodlabs.doktorumyanimda.model.reviews;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ReviewsEditRequest {
    private String patientID;
    private Long reviewID;
    private Integer rating;
    private String title;
    private String body;

    public boolean isValid() {
        return patientID != null && reviewID != null && rating != null && rating >= 1 && rating <= 5
                && title != null && !title.isEmpty()
                && body != null && !body.isEmpty();
    }
}