package com.kodlabs.doktorumyanimda.model.reviews;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReviewReply {
    private Long id;
    private Long reviewId;
    private String doctorId;
    private String body;
    private Date createdAt;
    private Date updatedAt;

    public ReviewReply(Long id, Long reviewId, String doctorId, String body, Date createdAt) {
        this.id = id;
        this.reviewId = reviewId;
        this.doctorId = doctorId;
        this.body = body;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }
}
