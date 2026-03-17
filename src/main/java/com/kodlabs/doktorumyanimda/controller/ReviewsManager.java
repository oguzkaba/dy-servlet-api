package com.kodlabs.doktorumyanimda.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.dal.IReviewsDal;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.reviews.DoctorSummary;
import com.kodlabs.doktorumyanimda.model.reviews.Review;
import com.kodlabs.doktorumyanimda.model.reviews.ReviewCreateRequest;
import com.kodlabs.doktorumyanimda.model.reviews.ReviewReplyRequest;
import com.kodlabs.doktorumyanimda.model.reviews.ReviewsEditRequest;
import com.kodlabs.doktorumyanimda.utils.Role;
import com.kodlabs.doktorumyanimda.utils.TextUtils;

public class ReviewsManager {
    private final IReviewsDal reviewsDal;

    public ReviewsManager(IReviewsDal reviewsDal) {
        this.reviewsDal = reviewsDal;
    }

    public ResponseEntity addReview(ReviewCreateRequest request, HttpServletRequest hsr) {
        if (!request.isValid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return reviewsDal.addReview(request);

        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity editReview(ReviewsEditRequest request,
            HttpServletRequest hsr) {
        if (!request.isValid()) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return reviewsDal.editReview(request);

        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity deleteReview(String requesterId, Long reviewId, boolean isAdmin) {
        if (requesterId == null || requesterId.isEmpty() || reviewId == null) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            // Admin control
            if (isAdmin) {
                if (Managers.userManager.isExistsUser(requesterId, Role.ADMIN.value())) {
                    return reviewsDal.deleteReview(requesterId, reviewId, true);
                } else {
                    return new ResponseEntity(false, ErrorMessages.notPermission);
                }
            } else if (Managers.userManager.isExistsUser(requesterId, Role.PATIENT.value())) {
                return reviewsDal.deleteReview(requesterId, reviewId, false);
            } else {
                return new ResponseEntity(false, ErrorMessages.notPermission);
            }
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntity replyReview(ReviewReplyRequest request) {
        if (!request.isValid()) {
            return new ResponseEntity(false, ErrorMessages.inValidData);
        }
        try {
            return reviewsDal.replyReview(request);
        } catch (ConnectionException e) {
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<List<Review>> getDoctorReviews(String doctorID, int page, int pageSize) {
        if (TextUtils.isEmpty(doctorID) || page <= 0 || pageSize <= 0) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            if (!Managers.userManager.isExistsUser(doctorID, Role.DOCTOR.value())) {
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
            return reviewsDal.getDoctorReviews(doctorID, page, pageSize);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<DoctorSummary> getDoctorSummary(String doctorID) {
        if (TextUtils.isEmpty(doctorID)) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            return reviewsDal.getDoctorSummary(doctorID);
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    public ResponseEntitySet<Review> getReviewByAppointment(Long appointmentID, String requesterId, boolean isAdmin) {
        if (requesterId == null || requesterId.isEmpty() || appointmentID == null) {
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try {
            // Admin control
            if (isAdmin) {
                if (Managers.userManager.isExistsUser(requesterId, Role.ADMIN.value())) {
                    return reviewsDal.getReviewByAppointment(appointmentID, requesterId, true);
                } else {
                    return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
                }
            } else if (Managers.userManager.isExistsUser(requesterId, Role.PATIENT.value())) {
                return reviewsDal.getReviewByAppointment(appointmentID, requesterId, false);
            } else if (Managers.userManager.isExistsUser(requesterId, Role.DOCTOR.value())) {
                return reviewsDal.getReviewByAppointment(appointmentID, requesterId, false);
            } else {
                return new ResponseEntitySet<>(false, ErrorMessages.notPermission);
            }
        } catch (ConnectionException e) {
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

}
