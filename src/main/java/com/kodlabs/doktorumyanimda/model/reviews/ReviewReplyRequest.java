package com.kodlabs.doktorumyanimda.model.reviews;

import com.kodlabs.doktorumyanimda.utils.TextUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ReviewReplyRequest {
    private String doctorID;
    private Long reviewID;
    private String body;

    public boolean isValid() {
        return !TextUtils.isEmpty(doctorID)
                && reviewID != null
                && !TextUtils.isEmpty(body);
    }
}
