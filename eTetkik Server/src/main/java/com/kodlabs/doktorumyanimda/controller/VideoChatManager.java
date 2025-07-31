package com.kodlabs.doktorumyanimda.controller;

import com.kodlabs.doktorumyanimda.videoChat.RtcTokenBuilder;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.videochat.CreateSessionRequest;
import com.kodlabs.doktorumyanimda.utils.Role;

public class VideoChatManager {
    private static String appId = "dfd5862179664b269ee88b1aa18c0042";
    private static String appCertificate = "15b3cd68f5694d139f4cdae79a3156c8";
    private static String channelName;
    private static int uid = 0;
    private static int expirationTimeInSeconds = 3600;


    public ResponseEntitySet<String> createToken(CreateSessionRequest request){
        if(!request.isValid()){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        try{
            if(Managers.userManager.isExistsUser(request.getUserID(), Role.DOCTOR.value())){
                return connect(request);
            }else{
                return new ResponseEntitySet<>(false, ErrorMessages.notAccessUserInformation);
            }
        }catch (ConnectionException e){
            return new ResponseEntitySet<>(false, e.getLocalizedMessage());
        }
    }

    private ResponseEntitySet<String> connect(CreateSessionRequest request){
        RtcTokenBuilder.Role role;
        if (request.getRole() == 1){
            role = RtcTokenBuilder.Role.Role_Publisher;
        }else{
            role = RtcTokenBuilder.Role.Role_Attendee;
        }
        channelName = request.getUserID();
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        return token.buildTokenWithUid(appId, appCertificate,
                channelName, uid, role, timestamp);
    }
}
