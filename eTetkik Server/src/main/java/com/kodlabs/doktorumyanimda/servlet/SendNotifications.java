package com.kodlabs.doktorumyanimda.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "SendNotifications",urlPatterns = "/admin/control/notification/send")
public class SendNotifications extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        req.getRequestDispatcher("/web/patients.jsp").include(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        String title = req.getParameter("title");
        String message = req.getParameter("message");
        String userID = req.getParameter("userID");
        String[] patients = req.getParameterValues("patients");
        List<String> errorPatients = new ArrayList<>();
        HttpSession session = req.getSession();
        /*if(patients != null && patients.length > 0){
            WebAdminSendNotificationLogResponse sendResponse = Managers.notificationManager.webAdminSendNotificationLog(new WebAdminSendNotificationAddLog(title,message,userID,0));
            if(sendResponse.isSuccess){
                for (String patient: patients){
                    List<String> notifications = Managers.notificationManager.getNotificationIDs(patient);
                    NotificationData notificationData = new NotificationData();
                    notificationData.message = message;
                    notificationData.playerIDs = notifications;
                    NotificationLog logData = new NotificationLog();
                    logData.setTitle(title);
                    logData.setUserID(patient);
                    logData.setBody(message);
                    Map<String,Object> attributes = new HashMap<>();
                    attributes.put("type",NotificationType.WEB_ADMIN_NOTICE.ordinal());
                    attributes.put("send_log_id", sendResponse.logID);
                    logData.setAttributes(attributes);
                    ResponseEntitySet<String> response = Managers.notificationManager.addNotificationLog(logData,false);
                    if(response.isSuccess){
                        notificationData.message = title + "\n" + message;
                        attributes.put("title",title);
                        attributes.put("message",message);
                        attributes.put("log_id",response.getData());
                        notificationData.data = attributes;
                        boolean isSuccess = IntegrationsFactory.getIntegrations(notificationData,IntegrationsFactory.NOTIFICATION).sendMessage();
                        session.setAttribute("isSuccess",isSuccess);
                        if(isSuccess){
                            session.setAttribute("message","Bildirim başarıyla gönderildi.");
                        }else{
                            session.setAttribute("message","İşlem başarısız lütfen tekrar deneyin.");
                        }
                    }else{
                        errorPatients.add(patient);
                    }
                }
            }else{
                session.setAttribute("isSuccess",false);
                session.setAttribute("message",sendResponse.message);
            }
        }else{
            session.setAttribute("isSuccess",false);
            session.setAttribute("message","Hasta bilgisi alınamadı");
        }
        if(!errorPatients.isEmpty()){
            session.setAttribute("isSuccess",false);
            session.setAttribute("error_patients",errorPatients);
        }

         */
        resp.sendRedirect(req.getContextPath() + "/admin/control/main");
    }
}
