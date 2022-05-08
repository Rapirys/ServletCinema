package com.servlet.cinema.framework.data;

import com.servlet.cinema.application.model.repository.OrderRepository;
import com.servlet.cinema.application.model.repository.SessionRepository;
import com.servlet.cinema.framework.exaptions.RepositoryException;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * Class which used contains basic logic of work with transactions.
 */
public class Dao {

    public Connection connection;


    public void beginTransaction(){
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException();
        }
    }

    public void endTransaction(){
        try {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

    public void close(){
        ConnectionPool.getInstance().close(connection);
    }


}
