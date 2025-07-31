package com.kodlabs.doktorumyanimda.servlet.filter;

import com.kodlabs.doktorumyanimda.utils.Functions;
import com.kodlabs.doktorumyanimda.utils.TextUtils;
import com.kodlabs.doktorumyanimda.controller.Managers;
import com.kodlabs.doktorumyanimda.dal.ConnectionException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserLogin implements Filter {
    private String contextPath;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.contextPath = filterConfig.getServletContext().getContextPath();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String userID = Functions.getCookieValue(request,"userID");
        String role = Functions.getCookieValue(request,"role");
        boolean isSuccess;
        if(!TextUtils.isEmpty(userID) && !TextUtils.isEmpty(role)){
            if(role.equalsIgnoreCase("0") || role.equalsIgnoreCase("1")){
                try {
                    boolean isAvailable = Managers.userManager.isExistsUser(userID,Byte.parseByte(role));
                    isSuccess = isAvailable;
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
            if(role.equalsIgnoreCase("0"))
                response.sendRedirect(contextPath.concat("/patient/main"));
            else
                response.sendRedirect(contextPath.concat("/doctor/main"));
        }else{
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {}
}
