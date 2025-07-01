package it.polimi.tiwpaolobrusa.controllers.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;

@WebFilter("/*")
public class LoginFilter implements Filter {

    private static final String[] paths = {"/Homepage", "/Vendo", "/css/aste.css", "/css/homepage.css"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request  = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI().substring(request.getContextPath().length());
        if (path.equals("/Login") || path.equals("/css/login.css")) {
            filterChain.doFilter(request, response);
        }
        else if (request.getSession().getAttribute("user") != null && request.getSession(false) != null && Arrays.asList(paths).contains(path)){
            filterChain.doFilter(request, response);
        }
        else if (request.getSession().getAttribute("user") != null && request.getSession(false) != null) {
            response.sendRedirect(request.getContextPath() + "/Homepage");
        }
        else {
            response.sendRedirect(request.getContextPath() + "/Login");
        }
    }

}
