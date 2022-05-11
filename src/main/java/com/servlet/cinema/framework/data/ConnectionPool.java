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
    public static final int INIT_CAPACITY = 5;
    public static final int MAX_CAPACITY = 30;
    private static final String url;
    private static final String user;
    private static final String password;
    private final static Logger logger = Logger.getLogger(ConnectionPool.class);
    private static final ConnectionPool instance = new ConnectionPool();
    private static final Stack<Connection> connections = new Stack<>();
    private static int given = 0;

    public static ConnectionPool getInstance() {
        return instance;
    }


    public synchronized Connection getConnection() {
        if (connections.size() > 0) {
            Connection connection = connections.pop();
            given++;
            return connection;
        } else if (given < MAX_CAPACITY) {
            try {
                Connection connection = DriverManager.getConnection(url, user, password);
                given++;
                return connection;
            } catch (SQLException e) {
                logger.error("Problem to get connection from DB");
                e.printStackTrace();
                throw new ConnectionPoolException("Problem to get connection from DB", e);
            }
        } else {
            logger.debug("Tread wait to connection");
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new ConnectionPoolException("Tread which wait for new connection wos interrupt", e);
            }
            return getConnection();
        }
    }

    public synchronized void close(Connection connection) {
        try {
            if (!connection.getAutoCommit()) {
                connection.rollback();
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connections.size() >= INIT_CAPACITY) {
            try {
                connection.close();
                given--;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else connections.push(connection);
        notify();
    }

    static {
        Properties property = AppContext.property;
        url = property.getProperty("datasource.url");
        user = property.getProperty("datasource.username");
        password = property.getProperty("datasource.password");

        for (int i = 0; i < INIT_CAPACITY; i++) {
            try {
                connections.push(DriverManager.getConnection(url, user, password));
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

    public int getSize() {
        return connections.size();
    }
}
