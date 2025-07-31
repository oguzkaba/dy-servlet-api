package com.kodlabs.doktorumyanimda.servlet.filter;

import com.kodlabs.doktorumyanimda.utils.TextUtils;
import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DoctorControl implements Filter {
    private String contextPath;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.contextPath = filterConfig.getServletContext().getContextPath();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String userID = request.getParameter("userID");
        String roleStr = request.getParameter("role");
        boolean isSuccess;
        if(!TextUtils.isEmpty(userID) && !TextUtils.isEmpty(roleStr)){
            if(roleStr.equalsIgnoreCase("1")){
                try {
                    if(Managers.userManager.isExistsUser(userID,Byte.parseByte(roleStr))){
                        isSuccess = false;
                    }else{
                        isSuccess = false;
                    }
                } catch (ConnectionException e) {
                    isSuccess = false;
                }
            }else{
                isSuccess = false;
            }
        }else{
            isSuccess = false;
        }
        if(isSuccess){
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            response.sendRedirect(contextPath.concat("/account/login"));
        }
    }

    @Override
    public void destroy() {

    }
}
