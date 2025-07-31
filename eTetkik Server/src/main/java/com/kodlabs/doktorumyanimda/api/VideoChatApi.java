package com.kodlabs.doktorumyanimda.api;


import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.videochat.CreateSessionRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/videoCall")
public class VideoChatApi {

    @Path("/token")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<String> token(@QueryParam("userID")String userID, @QueryParam("role")byte role){
        return Managers.videoChatManager.createToken(new CreateSessionRequest(userID, role));
    }

}
