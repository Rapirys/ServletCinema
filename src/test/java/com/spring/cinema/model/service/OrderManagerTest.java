package com.spring.cinema.model.service;


import com.servlet.cinema.application.entities.Order;
import com.servlet.cinema.application.entities.Session;
import com.servlet.cinema.application.entities.Ticket;
import com.servlet.cinema.application.model.service.OrderManager;
import com.servlet.cinema.framework.Util.AppContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderManagerTest {
    OrderManager orderManager = OrderManager.getInstance();

    @BeforeAll
    static void init() throws FileNotFoundException {
        ServletContextEvent servletContextEvent = mock(ServletContextEvent.class);
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getResourceAsStream("Hall_topology.txt"))
                .thenReturn(new FileInputStream("src/main/webapp/Hall_topology.txt"));
        when(servletContextEvent.getServletContext()).thenReturn(servletContext);
        AppContext.servletContextEvent = servletContextEvent;
    }

    @Test
    void generateTickets() {
        List<String> data = Arrays.asList("1_1", "2_2", "3_3", "4_4");
        Order order = new Order();
        order.setOrder_id(1L);
        order.setSession(new Session().setSession_id(2L));
        List<Ticket> tickets = orderManager.generateTickets(data, order);
        int i = 0;
        for (Ticket ticket : tickets) {
            i++;
            assertEquals(i, ticket.getPlace());
            assertEquals(i, ticket.getRow());
            assertEquals(ticket.getOrder_id(), 1L);
            assertEquals(ticket.getSession_id(), 2L);
        }
    }

}