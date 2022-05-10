package com.servlet.cinema.application.controller;

import com.servlet.cinema.application.controller.LoginController;
import com.servlet.cinema.framework.web.Model;
import com.servlet.cinema.framework.web.RedirectAttributes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {


    @Test
    void loginPage() {
        LoginController loginController = new LoginController();
        Model model = mock(Model.class);
        String[] s ={"aaa","asddas"};
        assertEquals(loginController.loginPage(s,model),"/login.jsp");
        verify(model).addAttribute("message", s);
    }

}