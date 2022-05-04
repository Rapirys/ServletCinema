package com.servlet.cinema.application.model.repository;

import com.servlet.cinema.application.entities.Film;
import com.servlet.cinema.application.entities.Order;
import com.servlet.cinema.application.entities.Session;
import com.servlet.cinema.application.entities.User;
import com.servlet.cinema.framework.data.ConnectionPool;
import com.servlet.cinema.framework.data.Dao;
import com.servlet.cinema.framework.exaptions.RepositoryException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;


public class OrderRepository extends Dao {
    private final static Logger logger = Logger.getLogger(OrderRepository.class);


    private final static String deleteBySessionAndActiveFalseAndTimeBefore =
            "DELETE FROM orders o WHERE o.session_id = ? AND o.active = false AND o.time <= ?";



    public void deleteBySessionAndActiveFalseAndTimeBefore(Session session, LocalDateTime localDateTime){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteBySessionAndActiveFalseAndTimeBefore);
            preparedStatement.setLong(1, session.getSession_id());
            preparedStatement.setTimestamp(2,Timestamp.valueOf(localDateTime));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete order", e);
            throw new RepositoryException("Can't delete order");
        }
    };

    private final static String update =
            "UPDATE orders SET active =? WHERE order_id = ?";
    public void updateActive(Long order_id, Boolean active) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setBoolean(1, active);
            preparedStatement.setLong(2, order_id );
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Problem to create order",e);
            throw new RepositoryException("Problem to create order");
        }
    }

    private final static String save =
            "INSERT INTO orders (active,time,session_id,user_id) VALUES (?, ?, ?, ?) RETURNING order_id";
    public Order save(Order entity) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(save);
            preparedStatement.setBoolean(1, entity.isActive());
            preparedStatement.setTimestamp(2,Timestamp.valueOf(entity.getTime()) );
            preparedStatement.setLong(3, entity.getSession().getSession_id());
            preparedStatement.setInt(4, entity.getUser().getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            entity.setOrder_id(resultSet.getLong("order_id"));
        } catch (SQLException e) {
            logger.error("Problem to create order",e);
            throw new RepositoryException("Problem to create order");
        }
        return entity;
    }

    private final static String findById =
            " SELECT * FROM orders o" +
            " INNER JOIN usr u ON o.order_id = ? AND o.user_id = u.id ";
    public Optional<Order> findById(Long aLong) {
        Optional<Order> order = Optional.empty();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(findById);
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next())
                order=Optional.of(initOrder(resultSet));
        } catch (SQLException e) {
            logger.error("Can't find order",e);
            throw new RepositoryException();
        }
        return order;
    }


    private Order initOrder(ResultSet resultSet) throws SQLException {
        Order order = new Order();
        order.setOrder_id(resultSet.getLong("order_id"));
        order.setActive(resultSet.getBoolean("active"));
        order.setTime(resultSet.getTimestamp("time").toLocalDateTime());
        Long session_id =(resultSet.getLong("session_id"));
        SessionRepository sessionRepository = SessionRepository.bound(this);
        Session session = sessionRepository.findById(session_id).get();
        order.setSession(session);
        Long user_id = resultSet.getLong(6);
        Integer resultId = resultSet.getInt("user_id");
        boolean resultActive = resultSet.getBoolean("active");
        String resultEmail = resultSet.getString("email");
        String resultPass = resultSet.getString("password");
        String resultUsername = resultSet.getString("username");
        User user = new User(resultId, resultUsername, resultEmail, resultActive, resultPass);
        order.setUser(user);
        return order;
    }

    public OrderRepository() {connection = ConnectionPool.getInstance().getConnection();}
    private OrderRepository(Connection connection) {
        this.connection = connection;
    }
    public static OrderRepository bound(Dao dao){
        return new OrderRepository(dao.connection);
    };


}
