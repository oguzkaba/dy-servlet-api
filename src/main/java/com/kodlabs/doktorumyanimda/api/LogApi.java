package com.kodlabs.doktorumyanimda.api;


import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.log.Log;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/log")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LogApi {

    @GET
    public ResponseEntitySet<List<Log>> logs(@QueryParam("userID")String userID, @QueryParam("startDate")String startDate,
                                             @QueryParam("endDate")String endDate){
        return Managers.logManager.logs(userID, startDate, endDate);
    }
}
