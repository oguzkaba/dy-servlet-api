package com.kodlabs.doktorumyanimda.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.reviews.DoctorSummary;
import com.kodlabs.doktorumyanimda.model.reviews.Review;
import com.kodlabs.doktorumyanimda.model.reviews.ReviewCreateRequest;
import com.kodlabs.doktorumyanimda.model.reviews.ReviewReplyRequest;
import com.kodlabs.doktorumyanimda.model.reviews.ReviewsEditRequest;

@Path("/reviews")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReviewsApi {
    @POST
    @Path("/add")
    public ResponseEntity addReview(ReviewCreateRequest requestBody, @Context HttpServletRequest hsr) {
        return Managers.reviewsManager.addReview(requestBody, hsr);
    }

    @POST
    @Path("/edit")
    public ResponseEntity editReview(ReviewsEditRequest requestBody, @Context HttpServletRequest hsr) {
        return Managers.reviewsManager.editReview(requestBody, hsr);
    }

    @DELETE
    @Path("/delete")
    public ResponseEntity deleteReview(@QueryParam("requesterID") String requesterID,
            @QueryParam("reviewID") Long reviewID, @QueryParam("isAdmin") boolean isAdmin,
            @Context HttpServletRequest hsr) {
        return Managers.reviewsManager.deleteReview(requesterID, reviewID, isAdmin);
    }

    @POST
    @Path("/reply")
    public ResponseEntity replyReview(ReviewReplyRequest requestBody, @Context HttpServletRequest hsr) {
        return Managers.reviewsManager.replyReview(requestBody);
    }

    @GET
    @Path("/doctor/{doctorID}")
    public ResponseEntitySet<List<Review>> getDoctorReviews(@PathParam("doctorID") String doctorID,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("pageSize") @DefaultValue("10") int pageSize,
            @Context HttpServletRequest hsr) {
        return Managers.reviewsManager.getDoctorReviews(doctorID, page, pageSize);
    }

    @GET
    @Path("/doctor/summary/{doctorID}")
    public ResponseEntitySet<DoctorSummary> getDoctorSummary(@PathParam("doctorID") String doctorID,
            @Context HttpServletRequest hsr) {
        return Managers.reviewsManager.getDoctorSummary(doctorID);
    }

    @GET
    @Path("/appointment/{appointmentID}")
    public ResponseEntitySet<Review> getReviewByAppointment(@PathParam("appointmentID") Long appointmentID,
            @QueryParam("requesterID") String requesterID,
            @QueryParam("isAdmin") boolean isAdmin,
            @Context HttpServletRequest hsr) {
        return Managers.reviewsManager.getReviewByAppointment(appointmentID, requesterID, isAdmin);
    }
}