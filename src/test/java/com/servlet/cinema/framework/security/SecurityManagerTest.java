package com.servlet.cinema.framework.security;


import com.servlet.cinema.application.entities.Session;
import com.servlet.cinema.application.entities.User;
import com.servlet.cinema.framework.web.Model;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityManagerTest {

    @Test
    void addUserToSession() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        Model model = new Model(request, mock(HttpServletResponse.class));
        User user = new User();
        user.setUsername("Lore");
        SecurityManager.addUserToSession(model, user);

        verify(session,times(1) ).setAttribute("user", user);
    }

    @Test
    void getUserFromSession() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        User user = new User();
        user.setUsername("aaaa");
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getSession()).thenReturn(session);

        assertEquals(SecurityManager.getUserFromSession(request),user);

    }

    @Test
    void testGetUserFromSession() {
        HttpSession session = mock(HttpSession.class);
        User user = new User();
        user.setUsername("aaaa");
        when(session.getAttribute("user")).thenReturn(user);

        assertEquals(SecurityManager.getUserFromSession(session),user);
    }

    @Test
    void hasAccess() {

    }
}