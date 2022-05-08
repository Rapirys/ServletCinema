package com.servlet.cinema.application.controller;


import com.servlet.cinema.framework.exaptions.ControllerNotExist;

import com.servlet.cinema.framework.web.Model;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;


import javax.servlet.http.HttpServletRequest;


import static org.mockito.Mockito.*;


class MyErrorControllerTest {



    @Test
    void handleError404() {
        MyErrorController myErrorController = new MyErrorController();

        HttpServletRequest request = mock(HttpServletRequest.class);
        Model model = mock(Model.class);
        when(request.getAttribute("javax.servlet.error.exception")).thenReturn(new ControllerNotExist());
        try {
            Assertions.assertThat(myErrorController.handleError(request, model)).isEqualTo("error.jsp");
        } catch (Exception e){
            Assertions.fail("Error controller throw error" ,e);
        }
         verify(model).addAttribute("error", "404");
    }

    @Test
    void handleError403() {
        MyErrorController myErrorController = new MyErrorController();
        try {
            Assertions.assertThat(myErrorController.handleError403()).isEqualTo("error403.jsp");
        } catch (Exception e){
            Assertions.fail("Error controller throw error" ,e);
        }

    }
}