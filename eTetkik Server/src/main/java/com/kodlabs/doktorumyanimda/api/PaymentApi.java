package com.kodlabs.doktorumyanimda.api;

import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.payment.PaymentInitializeRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/payment")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PaymentApi {

    @POST
    @Path("/initialize")
    public ResponseEntity initializePayment(PaymentInitializeRequest request, @Context HttpServletRequest hsr) {
        return Managers.paymentManager.initializeCheckoutForm(request);
    }

    @GET
    @Path("/price/{appointmentId}")
    public ResponseEntity getPrice(@PathParam("appointmentId") Long appointmentId) {
        return Managers.paymentManager.getAppointmentPrice(appointmentId);
    }
}
