package com.kodlabs.doktorumyanimda.model.reviews;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Review {
    private Long id;
    private Long appointmentId;
    private String patientId;
    private String patientName;
    private String doctorId;
    private Integer rating;
    private String title;
    private String body;
    private Date createdAt;
    private Date updatedAt;
    private ReviewReply reply;

    public Review(Long id, String patientId, String patientName, String doctorId, Long appointmentId, Integer rating,
            String title, String body, ReviewReply reply, Date createdAt, Date updatedAt) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.rating = rating;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.reply = reply;
    }

}
