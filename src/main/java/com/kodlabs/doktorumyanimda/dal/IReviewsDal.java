package com.kodlabs.doktorumyanimda.dal;

import java.util.List;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.reviews.DoctorSummary;
import com.kodlabs.doktorumyanimda.model.reviews.Review;
import com.kodlabs.doktorumyanimda.model.reviews.ReviewCreateRequest;
import com.kodlabs.doktorumyanimda.model.reviews.ReviewReplyRequest;
import com.kodlabs.doktorumyanimda.model.reviews.ReviewsEditRequest;

public interface IReviewsDal {
        ResponseEntity addReview(ReviewCreateRequest request)
                        throws ConnectionException;

        ResponseEntity editReview(ReviewsEditRequest request)
                        throws ConnectionException;

        ResponseEntity deleteReview(String requesterID, Long reviewID, boolean isAdmin) throws ConnectionException;

        ResponseEntity replyReview(ReviewReplyRequest request) throws ConnectionException;

        ResponseEntitySet<List<Review>> getDoctorReviews(String doctorID, int page, int pageSize)
                        throws ConnectionException;

        ResponseEntitySet<DoctorSummary> getDoctorSummary(String doctorID) throws ConnectionException;

        ResponseEntitySet<Review> getReviewByAppointment(Long appointmentID, String requesterID,
                        boolean requesterIsAdmin) throws ConnectionException;

}
