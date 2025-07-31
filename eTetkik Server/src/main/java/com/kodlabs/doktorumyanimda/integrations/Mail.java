package com.kodlabs.doktorumyanimda.integrations;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.kodlabs.doktorumyanimda.utils.Common;
import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.model.integrations.MailData;
import com.kodlabs.doktorumyanimda.service.GmailService;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

class Mail implements IIntegrations{
    private MailData entity;
    public Mail(MailData data){
        this.entity = data;
    }
    @Override
    public boolean sendMessage() {
        try {
            MimeMessage mimeMessage = createMimeMessage();
            sendMessage(GmailService.getGmailService(),mimeMessage);
            return true;
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    private MimeMessage createMimeMessage() throws MessagingException, UnsupportedEncodingException {
        Properties properties = new Properties();
        Session session = Session.getInstance(properties, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(getAddress(entity.type));
        message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(entity.address));
        message.setSubject(entity.title,"utf-8");
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(getContent(entity), "text/html;charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);
        message.setContent(multipart);
        return message;
    }
    private Message createMessageWithEmail(MimeMessage mailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        mailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    private Message sendMessage(Gmail service, MimeMessage emailContent) throws MessagingException, IOException {
        Message message = createMessageWithEmail(emailContent);
        message = service.users().messages().send("me", message).execute();
        return message;
    }
    private InternetAddress getAddress(String type) throws AddressException, UnsupportedEncodingException {
        InternetAddress address = null;
        switch (MailTypes.findType(type)){
            case SUPPORT:
            case VERIFY:
                address = new InternetAddress("support@azurerobot.com");
                address.setPersonal("destek");
                break;
        }
        return address;
    }
    private String getContent(MailData entity){
        final String path;
        String contentData;
        String data;
        switch (MailTypes.findType(entity.type)){
            case SUPPORT:
                path = Common.contentSource.concat( "/mail-content/reset-password.html");
                contentData =  Functions.getFileContent(path);
                data = String.format(contentData,entity.message);
                break;
            case VERIFY:
                path = Common.contentSource.concat( "/mail-contents/verification_email.html");
                contentData =  Functions.getFileContent(path);
                data = String.format(contentData, entity.message);
                break;
            default:
                data = "";
        }
        return data;
    }
}
