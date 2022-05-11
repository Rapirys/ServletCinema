package com.servlet.cinema.framework.security;

import com.servlet.cinema.application.entities.User;
import com.servlet.cinema.framework.web.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

public class SecurityManager {

    public static void addUserToSession(Model model, User user) {
        HttpSession session = model.request.getSession();
        session.setAttribute("user", user);
    }

    public static User getUserFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (User) session.getAttribute("user");
    }

    public static User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    public static boolean hasAccess(HttpServletRequest request, Set<GrantedAuthority> authorities) {
        AuthorityMapping authorityMapping = AuthorityMapping.getInstance();
        String path = request.getRequestURI();
        Set<String> requiredAuthorities;
        if (request.getMethod().equals("POST"))
            requiredAuthorities = authorityMapping.getPost(path);
        else requiredAuthorities = authorityMapping.getGet(path);
        Set<String> t = new HashSet<>();
        for (GrantedAuthority ga : authorities)
            t.add(ga.getAuthority());
        for (String requiredAuthority : requiredAuthorities)
            if (!t.contains(requiredAuthority))
                return false;
        return true;
    }

    public static void logout(Model model) {
        HttpSession session = model.request.getSession();
        session.removeAttribute("user");
    }
}
