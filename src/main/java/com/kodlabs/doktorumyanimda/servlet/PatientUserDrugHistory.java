package com.kodlabs.doktorumyanimda.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PatientUserDrugHistory",urlPatterns = "/patient/userDrug/list")
public class PatientUserDrugHistory extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        req.getRequestDispatcher("/web/user_drug_list.jsp").include(req,resp);
    }
}
