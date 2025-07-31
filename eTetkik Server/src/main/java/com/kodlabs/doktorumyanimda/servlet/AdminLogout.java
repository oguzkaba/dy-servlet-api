package com.kodlabs.doktorumyanimda.servlet;

import com.kodlabs.doktorumyanimda.utils.Functions;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "admin_logout",urlPatterns = "/admin/control/logout")
public class AdminLogout extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        resp.addCookie(Functions.addCookie("userID",""));
        resp.addCookie(Functions.addCookie("role",""));
        resp.sendRedirect(req.getContextPath().concat("/admin/login"));
    }
}
