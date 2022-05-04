package com.servlet.cinema.framework.security;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        UserDetails user = (UserDetails) request.getSession().getAttribute("user");
        Set<GrantedAuthority> authorities;
        if (user == null) {
            authorities = new HashSet<>();
        } else authorities = new HashSet<>(user.getAuthorities());
        if (SecurityManager.hasAccess(request, authorities)) {
            filterChain.doFilter(request, response);
        } else response.sendRedirect((user==null)?"/cinema/login":"/cinema/error403");
    }

    @Override
    public void destroy() {

    }
}
