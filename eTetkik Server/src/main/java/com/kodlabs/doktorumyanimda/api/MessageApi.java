package com.kodlabs.doktorumyanimda.api;


import com.kodlabs.doktorumyanimda.events.EventsFactory;
import com.kodlabs.doktorumyanimda.events.EventsFunctions;
import com.kodlabs.doktorumyanimda.events.EventsType;
import com.kodlabs.doktorumyanimda.events.IEvents;
import com.kodlabs.doktorumyanimda.events.azure.AzureEventsEntity;
import com.kodlabs.doktorumyanimda.messages.Messages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.message.Message;
import com.kodlabs.doktorumyanimda.model.message.MessageContent;
import com.kodlabs.doktorumyanimda.model.message.SendMessageRequest;
import com.kodlabs.doktorumyanimda.utils.*;
import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.model.message.*;
import com.kodlabs.doktorumyanimda.model.notification.NotificationLog;
import com.kodlabs.doktorumyanimda.notification.NotificationType;
import com.kodlabs.doktorumyanimda.notification.Notifications;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;

@Path("/message")
public class MessageApi {
    @Path("/send")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity sendMessage(@Context HttpServletRequest hsr, SendMessageRequest request){
        return Managers.messageManager.sendMessage(request, Functions.getClientIpAddress(hsr));
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<List<Message>> getAllMessages(@QueryParam("userID")String userID, @QueryParam("role")Byte role){
        return Managers.messageManager.getAllMessages(userID,role);
    }

    @GET
    @Path("/information")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<Message> getMessageInformation(@QueryParam("doctorID")String doctorID, @QueryParam("patientID")String patientID, @QueryParam("role")Byte role){
        return Managers.messageManager.getMessageInformation(doctorID, patientID, role);
    }


    @GET
    @Path("/isActive")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<Boolean> messageIsActive(@QueryParam("messageID")String messageID){
        return Managers.messageManager.messageIsActive(messageID);
    }

    @GET
    @Path("/content/list")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<List<MessageContent>> getMessageContent(@QueryParam("messageID")String messageID, @QueryParam("lastContentID")Integer lastContentID){
        int lastID = lastContentID == null ? -1 : lastContentID;
        return Managers.messageManager.getMessageContents(messageID,  lastID);
    }

    @PUT
    @Path("{id}/update")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity messageReadUpdate(@PathParam("id") String messageID, @QueryParam("isRead")Boolean isRead){
        return Managers.messageManager.updateMessageAttribute(messageID, "isRead", isRead);
    }

    @GET
    @Path("/unread/count")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntitySet<Integer> unReadMessageCount(@QueryParam("userID")String userID, @QueryParam("role")Byte role){
        return Managers.messageManager.isUnReadsMessageCount(userID, role);
    }
}
