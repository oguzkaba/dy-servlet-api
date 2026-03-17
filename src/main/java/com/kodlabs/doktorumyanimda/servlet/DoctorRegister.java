package com.kodlabs.doktorumyanimda.servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "doctorRegister",urlPatterns = "/admin/control/doctor/register")
public class DoctorRegister extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
        req.getRequestDispatcher("/web/doctor_register.jsp").include(req,resp);
    }
}
