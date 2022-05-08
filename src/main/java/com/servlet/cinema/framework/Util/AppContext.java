package com.servlet.cinema.framework.Util;

import javax.servlet.ServletContextEvent;
import java.util.Properties;


/**
 * Central class to provide configuration for an application.
 */
public class AppContext {
    public static Class<?> appClass;
    public static Properties property;
    public static ServletContextEvent servletContextEvent;
}
