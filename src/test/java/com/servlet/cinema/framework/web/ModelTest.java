package com.servlet.cinema.framework.web;


import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

public class ModelTest {

    @Test
    public void testMerge() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Model model = new Model(request, response);
        model.addAttribute("IntegerValue1", 1);
        model.addAttribute("StringValue1", Integer.valueOf("1"));
        model.merge();
        verify(request).setAttribute("IntegerValue1", 1);
        verify(request).setAttribute("StringValue1", Integer.valueOf("1"));
        verify(request, atMost(2)).setAttribute(any(), any());
    }
}