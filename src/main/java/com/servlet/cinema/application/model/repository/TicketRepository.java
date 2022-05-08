package com.servlet.cinema.application.model.repository;



import com.servlet.cinema.application.entities.Film;
import com.servlet.cinema.application.entities.Order;
import com.servlet.cinema.application.entities.Session;
import com.servlet.cinema.application.entities.Ticket;
import com.servlet.cinema.framework.data.ConnectionPool;
import com.servlet.cinema.framework.data.Dao;
import com.servlet.cinema.framework.exaptions.ConnectionPoolException;
import com.servlet.cinema.framework.exaptions.RepositoryException;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class TicketRepository extends Dao {
    private final static Logger logger = Logger.getLogger(TicketRepository.class);

    public static final String getHallBySession =" SELECT * FROM findHall(?) ORDER BY \"row\", place";


    public List<Ticket> getHallBySession(Long id){
        List<Ticket> ticket = new ArrayList<>();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(getHallBySession);
            preparedStatement.setLong(1, id);
            ResultSet resultSet=preparedStatement.executeQuery();
            initTicketList(resultSet, ticket);
        } catch (SQLException e) {
            logger.error("Can't find tickets",e);
            throw new RepositoryException("Can't find tickets");
        }
        return ticket;
    };

    private final static String findTicketsByOrder =
            "SELECT * FROM tickets WHERE order_id = ?";
    public List<Ticket> findTicketsByOrder(Order order){
        List<Ticket> tickets = new ArrayList<>();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(findTicketsByOrder);
            preparedStatement.setLong(1, order.getOrder_id());
            ResultSet resultSet=preparedStatement.executeQuery();
            initTicketList(resultSet, tickets);
        } catch (SQLException e) {
            logger.error("Can't find tickets",e);
            throw new RepositoryException("Can't find tickets");
        }
        return tickets;
    };

    private final static String countTicketByOrder =
            "SELECT count(*) FROM tickets WHERE order_id = ?";
    public int countTicketByOrder(Order order){
        int k;
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(countTicketByOrder);
            preparedStatement.setLong(1, order.getOrder_id());
            ResultSet resultSet=preparedStatement.executeQuery();
            resultSet.next();
            k=resultSet.getInt(1);
        } catch (SQLException e) {
            logger.error("Can't find tickets",e);
            throw new RepositoryException("Can't find tickets");
        }
        return k;
    };

    private void initTicketList(ResultSet resultSet, List<Ticket> filmSet) throws SQLException {
        while (resultSet.next())
            filmSet.add(initTicket(resultSet));
    }

    private Ticket initTicket(ResultSet resultSet) throws SQLException {
        Ticket ticket;
        ticket = new Ticket();
        ticket.setTicket_id(resultSet.getLong("ticket_id"));
        ticket.setPlace(resultSet.getInt("place"));
        ticket.setRow(resultSet.getInt("row"));
        ticket.setSalt(resultSet.getLong("salt"));
        ticket.setSession_id(resultSet.getLong("session_id"));
        ticket.setSession_id(resultSet.getLong("order_id"));
        return ticket;
    }

    private final static String findById =
            " SELECT * FROM tickets t WHERE t.ticket_id = ?";
    public Optional<Ticket> findById(Long aLong) {
        Optional<Ticket> order = Optional.empty();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(findById);
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next())
                order=Optional.of(initTicket(resultSet));
        } catch (SQLException e) {
            logger.error("Can't find ticket",e);
            throw new RepositoryException();
        }
        return order;
    }

    private final static String save =
            "INSERT INTO tickets (place, row, salt, order_id, session_id) VALUES (?, ?, ?, ?, ?) RETURNING ticket_id";
    public Ticket save(Ticket entity) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(save);
            preparedStatement.setInt(1, entity.getPlace());
            preparedStatement.setInt(2,entity.getRow());
            preparedStatement.setLong(3, entity.getSalt());
            preparedStatement.setLong(4, entity.getOrder_id());
            preparedStatement.setLong(5, entity.getSession_id());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            entity.setTicket_id(resultSet.getLong("ticket_id"));
        } catch (SQLException e) {
            logger.error("Problem to create order");
            throw new RepositoryException("Problem to create order");
        }
        return entity;
    }

    public void saveAll(List<Ticket> tickets) {
        for (Ticket ticket: tickets)
            save(ticket);
    }

    public TicketRepository() {connection = ConnectionPool.getInstance().getConnection();}
    private TicketRepository(Connection connection) {
        this.connection = connection;
    }
    public static TicketRepository bound(Dao dao){
        return new TicketRepository(dao.connection);
    };
}