package com.servlet.cinema.framework.data;


import com.servlet.cinema.framework.Util.AppContext;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Properties;


import static com.servlet.cinema.framework.Util.AppContext.servletContextEvent;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ConnectionPoolTest {
    @BeforeAll
    static void init() throws IOException {
        try (InputStream fis = new FileInputStream("src/main/webapp/framework.properties")) {
            Properties property = new Properties();
            property.load(fis);
            AppContext.property = property;
        }
    }


    @Test
    public void moreInitCapacity() {
        ConnectionPool connectionPool = ConnectionPool.getInstance();

        //get all init connections from CP
        ArrayList<Connection> connections = new ArrayList<>(5);
        for (int i = 0; i < ConnectionPool.INIT_CAPACITY; i++)
            connections.add(connectionPool.getConnection());
        assertEquals(0, connectionPool.getSize());
        Connection extraConnection = connectionPool.getConnection();
        assertNotNull(extraConnection);
        //returns connection
        connectionPool.close(extraConnection);
        for (int i = 0; i < ConnectionPool.INIT_CAPACITY; i++)
            connectionPool.close(connections.get(i));

        assertEquals(5, connectionPool.getSize());

    }

    @Test
    public void waitMultiTread() throws InterruptedException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        //get all  connections from CP
        ArrayList<Connection> connections = new ArrayList<>(5);
        for (int i = 0; i < ConnectionPool.MAX_CAPACITY; i++) {
            connections.add(connectionPool.getConnection());
        }
        Thread thread = new Thread(() -> {
            Connection connection = ConnectionPool.getInstance().getConnection();
            System.out.println(connection);
            ConnectionPool.getInstance().close(connection);
        });

        thread.start();
        connectionPool.close(connections.get(ConnectionPool.MAX_CAPACITY-1));
        thread.join(2000);
        boolean fail = false;
        if(thread.isAlive()) {
            thread.interrupt();
            fail=true;
        }
        for (int i = 0; i < ConnectionPool.MAX_CAPACITY-1; i++)
            connectionPool.close(connections.get(i));
        assertFalse(fail,"Deadlock or problem with DB");
    }
}