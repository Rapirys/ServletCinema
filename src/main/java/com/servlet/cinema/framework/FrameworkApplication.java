package com.servlet.cinema.framework;

import com.servlet.cinema.framework.Util.AppContext;
import com.servlet.cinema.framework.data.ConnectionPool;
import com.servlet.cinema.framework.security.AuthorityMapping;
import com.servlet.cinema.framework.web.HandlerMapping;
import org.apache.log4j.Logger;

import javax.servlet.ServletContextEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FrameworkApplication {
    private final static Logger logger = Logger.getLogger(FrameworkApplication.class);
    public  static void run(Class<?> appClass, ServletContextEvent servletContextEvent){
        AppContext.appClass = appClass;
        AppContext.servletContextEvent = servletContextEvent;
        try (InputStream fis = servletContextEvent.getServletContext().getResourceAsStream("framework.properties")){
            Properties property = new Properties();
            property.load(fis);
            AppContext.property=property;
        } catch (IOException e) {
            logger.error("Properties file not found.");
            e.printStackTrace();
        }
        init();
    }
    private static void init(){
        ConnectionPool.getInstance();
        HandlerMapping.getInstance();
        AuthorityMapping.getInstance();
    }
}
