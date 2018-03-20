package ru.otus.sokolovsky.hw15.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();
        Object login = session.getAttribute("login");
        if (login != null) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // main action of filter
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.sendRedirect("/login");
    }

    @Override
    public void destroy() {
    }
}
