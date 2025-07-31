package com.kodlabs.doktorumyanimda.api.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebFilter(filterName = "CharacterEncodingFilter", urlPatterns = {"/api/*"})
public class CharEncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig){}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        chain.doFilter(request, response);
    }
    @Override
    public void destroy() {}
}
