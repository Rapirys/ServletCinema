package com.servlet.cinema.framework.data;

import com.servlet.cinema.framework.Util.AppContext;
import com.servlet.cinema.framework.exaptions.ConnectionPoolException;
import org.apache.log4j.Logger;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Stack;

public final class ConnectionPool {
    private static final int INIT_CAPACITY = 5;
    private static final int MAX_CAPACITY = 30;
    private static  String url;
    private static String user;
    private static String password;
    private final static Logger logger = Logger.getLogger(ConnectionPool.class);
    private static final ConnectionPool instance = new ConnectionPool();
    private static final Stack<Connection> connections = new Stack<>();
    private static int given = 0;

    public static ConnectionPool getInstance(){
        return instance;
    }


    private static Class getClass(String classname)
            throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if(classLoader == null)
            classLoader = ConnectionPool.class.getClassLoader();
        return (classLoader.loadClass(classname));
    }

    public synchronized Connection getConnection(){
        if (connections.size()> 0) {
            Connection connection = connections.pop();
            given++;
            return connection;
        }else if (given<MAX_CAPACITY){
            try {
                Connection connection = connections.push(connections.push( DriverManager.getConnection(url, user, password)));
                given++;
                return connections.push(connections.push( DriverManager.getConnection(url, user, password)));
            } catch (SQLException e) {
                logger.error("Problem to get connection from DB");
                e.printStackTrace();
                throw new ConnectionPoolException("Problem to get connection from DB", e);
            }
        }else {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getConnection();
        }
    }
    public synchronized void close(Connection connection){
        try {
            if (!connection.getAutoCommit()) {
                connection.rollback();
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connections.size()>INIT_CAPACITY) {
            try {
                connection.close();
                given--;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else connections.push(connection);
        notify();
    }

    static {
        Properties property = AppContext.property;
        url = property.getProperty("datasource.url");
        user = property.getProperty("datasource.username");
        password = property.getProperty("datasource.password");

        for (int i = 0; i<INIT_CAPACITY; i++){
            try {
                connections.push( DriverManager.getConnection(url, user, password));
            } catch (Exception e) {
                logger.error("Cant create connection during initialization");
                e.printStackTrace();
            }
        }
    }


    public void commit(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            logger.error("Faille to commit connection", e);
            e.printStackTrace();
        }
    }

    private ConnectionPool() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
