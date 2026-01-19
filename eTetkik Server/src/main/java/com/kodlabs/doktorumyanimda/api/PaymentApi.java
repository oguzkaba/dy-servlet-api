package com.kodlabs.doktorumyanimda.api;

import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.payment.PaymentInitializeRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
}
