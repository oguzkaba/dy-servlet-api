package com.kodlabs.doktorumyanimda.api;


import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.integrations.IIntegrations;
import com.kodlabs.doktorumyanimda.integrations.IntegrationsFactory;
import com.kodlabs.doktorumyanimda.integrations.MailTypes;
import com.kodlabs.doktorumyanimda.messages.ErrorMessages;
import com.kodlabs.doktorumyanimda.messages.Messages;
import com.kodlabs.doktorumyanimda.model.ResponseEntity;
import com.kodlabs.doktorumyanimda.model.ResponseEntitySet;
import com.kodlabs.doktorumyanimda.model.contact.Contact;
import com.kodlabs.doktorumyanimda.model.integrations.MailData;
import com.kodlabs.doktorumyanimda.model.integrations.SmsData;
import com.kodlabs.doktorumyanimda.model.user.*;
import com.kodlabs.doktorumyanimda.service.RecaptchaService;
import com.kodlabs.doktorumyanimda.utils.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserApi {

    @Path("/exists")
    @GET
    public ResponseEntity exists(@QueryParam("uname")String uname, @QueryParam("role")byte role){
        return Managers.userManager.existsUser(uname, role);
    }

    @Path("/contact")
    @GET
    public ResponseEntitySet<Contact> contactInformation(@QueryParam("uname")String userID, @QueryParam("role")byte role){
        return Managers.userManager.contactInformation(userID,role);
    }

    @Path("/{uname}/{role}/available/contact")
    @GET
    public ResponseEntitySet<Contact> userAvailableContact(@PathParam("uname")String uname, @PathParam("role")byte role){
        ResponseEntitySet<Contact> response = Managers.userManager.contactInformation(uname, role);
        if(response.isSuccess){
            Contact data = response.getData();
            data.setMail(Functions.mailConvertSecurityFormat(data.getMail()));
            data.setPhone(Functions.phoneConvertSecurityFormat(data.getPhone()));
        }
        return response;
    }
    @Path("/{userID}/{role}/password/verify")
    @GET
    public ResponseEntitySet<Boolean> userPasswordVerify(@PathParam("userID")String userID, @PathParam("role")byte role, @QueryParam("password") String password){
        return Managers.userManager.userPasswordVerify(userID, role, password);
    }

    @Path("/signup/phone/exists")
    @GET
    public ResponseEntity singUpPhoneExists(@QueryParam("phone")String phone, @QueryParam("role")byte role, @QueryParam("verifyToken")String verifyToken){
        try{
            if(RecaptchaService.getInstance().captchaVerify(verifyToken)){
                ResponseEntitySet<Boolean> response = Managers.userManager.signUpPhoneControl(phone, role);
                if(response.isSuccess){
                    if(!response.getData()){
                        String code = Functions.generateCode();
                        IIntegrations integration = IntegrationsFactory.getIntegrations(new SmsData(String.format(Messages.smsLoginVerifyMessage, code), phone), IntegrationsFactory.SMS);
                        if(integration != null){
                            if(!integration.sendMessage()){
                                response.isSuccess = false;
                                response.message = ErrorMessages.smsSendFailed;
                            }else{
                                Phones.setPhoneVerifications(phone, code);
                                return new ResponseEntity();
                            }
                        }else{
                            response.isSuccess = false;
                            response.message = ErrorMessages.operationFailed;
                        }
                    }else{
                        return new ResponseEntity(false, ErrorMessages.availablePhoneNumber);
                    }
                }
                return response;
            }else{
                return new ResponseEntity(false, ErrorMessages.notVerifyBot);
            }
        }catch (IOException e){
            return new ResponseEntity(false, e.getLocalizedMessage());
        }
    }
    @Path("/signup/phone/verify")
    @POST
    public ResponseEntity singUpPhoneVerify(PhoneVerifyRequest request){
        return Managers.userManager.singUpPhoneVerify(request);
    }
    @Path("/{userID}/{role}/change/password")
    @PUT
    public ResponseEntity changePassword(@PathParam("userID")String userID, @PathParam("role")byte role, ChangePassword data){
        return Managers.userManager.changePassword(userID, role, data);
    }
    @Path("/all")
    @GET
    public ResponseEntitySet<List<UserInformation>> allUser(@QueryParam("userID")String userID, @QueryParam("type") String type){
        return Managers.userManager.allUser(userID, type);
    }
    @Path("/{userID}/{role}/reset/password")
    @PUT
    public ResponseEntity resetPassword(@PathParam("userID") String userID, @PathParam("role")byte role, ResetPassword data){
        return Managers.userManager.resetPassword(userID, role, data);
    }


    @Path("/{role}/contact/address/control")
    @GET
    public ResponseEntity contactAddressControl(@PathParam("role")byte role, @QueryParam("address")String address, @QueryParam("type")String type){
        IIntegrations integration;
        String code = Functions.generateCode();
        switch (ContactType.findContactType(type)){
            case SMS:
                ResponseEntitySet<Boolean> response = Managers.userManager.signUpPhoneControl(address, role);
                if(response.isSuccess){
                    integration = IntegrationsFactory.getIntegrations(new SmsData(String.format(Messages.smsLoginVerifyMessage, code), address), IntegrationsFactory.SMS);
                    break;
                }else{
                    return response;
                }
            case MAIL:
                if(!Patterns.EMAIL.matcher(address).matches()){
                    return new ResponseEntity(false, ErrorMessages.inValidEmail);
                }
                integration = IntegrationsFactory.getIntegrations(new MailData(Messages.mailVerification, address, code, MailTypes.SUPPORT.getType()), IntegrationsFactory.MAIL);
                break;
            default:
                return new ResponseEntity(false, ErrorMessages.inValidContactType);
        }
        if(integration != null){
            if(integration.sendMessage()){
                ContactVerification.setVerification(address, code);
                return new ResponseEntity();
            }else{
                return new ResponseEntity(false, ErrorMessages.operationFailed);
            }
        }else{
            return new ResponseEntity(false, ErrorMessages.operationFailed);
        }
    }

    @Path("/contact/address/code/verify")
    @GET
    public ResponseEntity contactCodeVerify(@QueryParam("address")String address, @QueryParam("code")String code){
        return Managers.userManager.contactCodeVerify(address, code);
    }


    @Path("/send/verifyCode")
    @POST
    public ResponseEntity sendVerifyCode(SendVerifyCodeRequest request){
        if(request.isValid()){
            ResponseEntitySet<UserContactInformation> responseContact = Managers.userManager.userContactInformation(request.getUname(), request.getRole());
            if(responseContact.isSuccess){
                String code = Functions.generateCode();
                IIntegrations integrations;
                switch (ContactType.findContactType(request.getContactType())){
                    case SMS:
                        integrations = IntegrationsFactory.getIntegrations(new SmsData(String.format(Messages.passwordForgotCodeVerifyMessage, code), responseContact.getData().getPhone()), IntegrationsFactory.SMS);
                        break;
                    case MAIL:
                        integrations = IntegrationsFactory.getIntegrations(new MailData(Messages.passwordReset, responseContact.getData().getMail(), String.format(Messages.passwordForgotCodeVerifyMessage, code), MailTypes.SUPPORT.getType()),IntegrationsFactory.MAIL);
                        break;
                    default:
                        integrations = null;
                }
                if(integrations != null) {
                   boolean isSuccess = integrations.sendMessage();
                   if(isSuccess){
                       ForgotVerification.setVerification(request.getUname(), code);
                       return new ResponseEntity();
                   }else{
                       return new ResponseEntity(false, ErrorMessages.operationFailed);
                   }
                }else{
                    return new ResponseEntity(false, ErrorMessages.operationFailed);
                }
            }else{
                return responseContact;
            }
        }else{
             return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
    }

    @Path("/{uname}/{role}/forgot/code/verify")
    @GET
    public ResponseEntitySet<String> forgotCodeVerify(@PathParam("uname")String uname, @PathParam("role")byte role, @QueryParam("code")String code){
        if(TextUtils.isEmpty(uname) || TextUtils.isEmpty(code)){
            return new ResponseEntitySet<>(false, ErrorMessages.inValidData);
        }
        if(ForgotVerification.codeIsVerify(uname, code)){
            ResponseEntitySet<UserInformation> responseInformation = Managers.userManager.userInformation(uname, role);
            if(responseInformation.isSuccess){
                return new ResponseEntitySet<>(
                        responseInformation.getData().getUserID()
                );
            }else{
                return new ResponseEntitySet<>(false, responseInformation.message);
            }
        }else{
            return new ResponseEntitySet<>(false, ErrorMessages.inValidVerifyCode);
        }
    }
}
