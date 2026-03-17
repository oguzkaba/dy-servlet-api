package com.kodlabs.doktorumyanimda.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "adminLogin",urlPatterns = "/admin/login")
public class AdminLogin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        req.getRequestDispatcher("/web/admin_login.jsp").include(req,resp);
    }

    /*@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userName = req.getParameter("userName");
        String password = req.getParameter("password");
        String remember_me = req.getParameter("remember_me");
        LoginRequest request = new LoginRequest();
        request.setRole(Role.ADMIN);
        request.setUname(userName);
        request.setPassword(Functions.toSHA1(password));
        ResponseEntitySet<UserAdmin> loginResponse = Managers.userManager.login(request);
        if(loginResponse.isSuccess){
            if(remember_me != null && remember_me.equalsIgnoreCase("on")){
                resp.addCookie(Functions.addCookie("admin_username",userName));
                resp.addCookie(Functions.addCookie("admin_password",password));
                resp.addCookie(Functions.addCookie("admin_remember",remember_me));
            }else{
                resp.addCookie(Functions.addCookie("admin_username",""));
                resp.addCookie(Functions.addCookie("admin_password",""));
                resp.addCookie(Functions.addCookie("admin_remember",""));
            }
            resp.addCookie(Functions.addCookie("userID",loginResponse.getData().getUserID()));
            resp.addCookie(Functions.addCookie("role",Byte.toString(Role.ADMIN)));
            resp.sendRedirect(req.getContextPath().concat("/admin/control/main"));
        }else{
            HttpSession session = req.getSession();
            session.setAttribute("isSuccess",false);
            session.setAttribute("message",loginResponse.message);
            resp.sendRedirect(req.getContextPath().concat("/admin/login"));
        }
    }

     */
}
