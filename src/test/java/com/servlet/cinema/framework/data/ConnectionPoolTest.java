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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class ConnectionPoolTest {
    @BeforeAll
    static void init() throws IOException {
        try (InputStream fis = new FileInputStream("src/main/webapp/framework.properties")){
            Properties property = new Properties();
            property.load(fis);
            AppContext.property=property;
        }
    }


   @Test
   public void moreInitCapacity(){
       ConnectionPool connectionPool = ConnectionPool.getInstance();

       //get all init connections from CP
       ArrayList<Connection> connections = new ArrayList<>(5);
       for (int i=0; i<5; i++)
           connections.add(connectionPool.getConnection());
       assertEquals(0, connectionPool.getSize());
       //get 1 more connection from another thread.
       Connection extraConnection  = connectionPool.getConnection();
       assertNotNull(extraConnection);
       //returns connection
       connectionPool.close(extraConnection);
       for (int i=0; i<5; i++)
           connectionPool.close(connections.get(i));

       assertEquals(5, connectionPool.getSize());


   }
}