package com.kodlabs.doktorumyanimda.api;


import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.notification.NotificationLog;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/notification")
public class NotificationApi {

    @Path("/push")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity pushNotification(@QueryParam("userID")String userID, @QueryParam("notificationID") String notificationID){
        return Managers.notificationManager.pushNotification(userID,notificationID);
    }
    @Path("/cancel")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity cancelNotification(@QueryParam("notificationID")String notificationID){
        return Managers.notificationManager.cancelNotification(notificationID);
    }

    @Path("/list")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<List<NotificationLog>> getAllNotificationsLog(@QueryParam("userID")String userID, @QueryParam("role")byte role){
        return Managers.notificationManager.getAllNotificationLogs(userID, role);
    }
    @Path("/count")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<Integer> notificationCount(@QueryParam("userID")String userID, @QueryParam("role")Byte role){
        return Managers.notificationManager.notificationCount(userID, role);
    }

    @Path("/{role}/log/{id}/delete")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity deleteNotificationLog(@PathParam("id")String logID, @PathParam("role")byte role){
        return Managers.notificationManager.deleteNotificationLog(logID, role);
    }

    @Path("/{role}/log/{id}/read")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity readNotificationLog(@PathParam("id")String logID, @PathParam("role")byte role){
        return Managers.notificationManager.readNotificationLog(logID, role);
    }
}
