package com.servlet.cinema.framework.web;


import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.servlet.cinema.framework.web.ViewResolver.processView;
import static org.mockito.Mockito.*;

public class ViewResolverTest {

    @Test
    public void testProcessViewHttpOk() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Model model = new Model(request, response);
        RedirectAttributes ra = mock(RedirectAttributes.class);
        processView("HttpStatus.ok", model, ra);
        verify(request, never()).getContextPath();
        verify(response, never()).sendRedirect(any());
        verify(request, never()).getRequestDispatcher(any());
    }


    @Test
    public void testProcessViewForward() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RedirectAttributes ra = mock(RedirectAttributes.class);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        Model model = new Model(request, response);

        when(request.getRequestDispatcher(any())).thenReturn(requestDispatcher);

        processView("test.jsp", model, ra);
        verify(request, never()).getContextPath();
        verify(response, never()).sendRedirect(any());
        verify(request).getRequestDispatcher("/WEB-INF/templates/test.jsp");
    }
}