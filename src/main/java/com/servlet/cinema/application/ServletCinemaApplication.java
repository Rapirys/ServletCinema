package com.servlet.cinema.application;


import com.servlet.cinema.framework.FrameworkApplication;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletCinemaApplication implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        FrameworkApplication.run(ServletCinemaApplication.class, servletContextEvent);

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
