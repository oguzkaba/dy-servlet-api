package com.kodlabs.doktorumyanimda.api;

import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.messages.Messages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.notification.NotificationLog;
import com.kodlabs.doktorumyanimda.model.peak.*;
import com.kodlabs.doktorumyanimda.utils.*;
import com.kodlabs.doktorumyanimda.notification.NotificationType;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/peak")
public class PeakApi {
    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<String> create(@Context HttpServletRequest hsr, CreatePeakRequest request,
            @QueryParam("version") String version, @QueryParam("os") String os) {
        return Managers.peakManager.create(request, version, os, hsr);
    }

    @GET
    @Path("/google/product/list")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<List<GoogleProduct>> googleProductList() {
        return Managers.peakManager.googleProductList();
    }

    @GET
    @Path("/doctor/first/visit/isFree")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<Boolean> isFirstVisitFree(@QueryParam("patientID") String patientID,
            @QueryParam("doctorID") String doctorID) {
        return Managers.peakManager.isFirstVisitFree(patientID, doctorID);
    }

    @POST
    @Path("/pay")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity pay(PeakPayRequest request) {
        ResponseEntitySet<PeakPayInformation> response = Managers.peakManager.pay(request);
        if (response.isSuccess) {
            System.out.printf("Peak Pay: %s, message: %s \n", Functions.getDate(), "Peak Pay Success");
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("type", NotificationType.INFORMATION.ordinal());
            String fullName = response.getData().getPatientFullName();
            String doctorID = response.getData().getDoctorID();
            String title = Messages.peakPaymentTitle;
            String body = String.format(Messages.peakPaymentMessage, TextUtils.isEmpty(fullName) ? "" : fullName);
            NotificationLog log = new NotificationLog(title, body, attributes, null);
            log.setUserID(doctorID);
            NotificationUtils.sendNotifications(log, true, true);
        } else {
            System.out.printf("Peak Pay: %s, message: %s \n%n", Functions.getDate(), response.message);
        }
        return response;
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<List<Peak>> list(@QueryParam("userID") String userID, @QueryParam("role") byte role) {
        return Managers.peakManager.list(userID, role);
    }

    @GET
    @Path("/detail")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<Peak> list(@QueryParam("peakID") String peakID, @QueryParam("userID") String userID,
            @QueryParam("role") byte role) {
        return Managers.peakManager.detail(peakID, userID, role);
    }

    @POST
    @Path("/demand/verify")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity peakVerifyDemand(@Context HttpServletRequest hsr, PeakDemandVerifyRequest request) {
        return Managers.peakManager.peakDemandVerify(request, hsr);
    }

    @GET
    @Path("/fee/list")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<List<Integer>> peakFeeList() {
        return Managers.peakManager.feeList();
    }

    @GET
    @Path("/day/list")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<List<Integer>> peakDayList() {
        return Managers.peakManager.peakDayList();
    }

    @GET
    @Path("/patient/firstVisitPeakActive/status")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<String> firstVisitPeakActiveStatus(@QueryParam("patientID") String patientID) {
        return Managers.peakManager.firstVisitPeakActiveStatus(patientID);
    }
}
