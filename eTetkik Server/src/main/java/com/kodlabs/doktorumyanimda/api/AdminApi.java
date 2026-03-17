package com.kodlabs.doktorumyanimda.api;

import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.integrations.IIntegrations;
import com.kodlabs.doktorumyanimda.integrations.IntegrationsFactory;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.messages.Messages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.user.LoginData;
import com.kodlabs.doktorumyanimda.model.admin.user.AdminLoginRequest;
import com.kodlabs.doktorumyanimda.model.admin.user.AdminLoginVerifyRequest;
import com.kodlabs.doktorumyanimda.model.integrations.SmsData;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminApi {
    @POST
    @Path("/login")
    public ResponseEntity login(AdminLoginRequest request) {
        ResponseEntitySet<LoginData> response = Managers.adminManager.login(request);
        if (response.isSuccess) {
            IIntegrations integration = IntegrationsFactory.getIntegrations(
                    new SmsData(String.format(Messages.smsLoginVerifyMessage, response.getData().getCode()),
                            response.getData().getPhone()),
                    IntegrationsFactory.SMS);
            if (integration != null) {
                boolean result = integration.sendMessage();
                if (!result) {
                    return new ResponseEntity(false, ErrorMessages.operationFailed);
                }
                return new ResponseEntity();
            }
        }
        return new ResponseEntity(false, response.message);
    }

    @POST
    @Path("/login/code/verify")
    public Response loginVerifyCode(@Context HttpServletRequest hsr, AdminLoginVerifyRequest request) {
        if (request.isValid()) {
            return Response.ok().entity(Managers.adminManager.verifyCode(request, hsr)).build();
        }
        return Response.serverError().entity(ErrorMessages.inValidData).build();
    }
}
