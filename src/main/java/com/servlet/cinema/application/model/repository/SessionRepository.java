package com.servlet.cinema.application.model.repository;


import com.servlet.cinema.application.entities.Film;
import com.servlet.cinema.application.entities.Session;
import com.servlet.cinema.framework.data.ConnectionPool;
import com.servlet.cinema.framework.data.Dao;
import com.servlet.cinema.framework.data.Pageable;
import com.servlet.cinema.framework.data.Sort;
import com.servlet.cinema.framework.exaptions.ConnectionPoolException;

import com.servlet.cinema.framework.exaptions.RepositoryException;
import org.apache.log4j.Logger;


import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class SessionRepository extends Dao {
    private final static Logger logger = Logger.getLogger(SessionRepository.class);


    private final static String findAllByTitleEnContains =
            "SELECT * FROM \"session\" s INNER JOIN film f  ON s.film_id = f.film_id WHERE upper(f.title_en) LIKE ?";
    public List<Session> findAllByFilmTitleEnContains(String search, Pageable pageable) {
        ArrayList<Session> sessions = new ArrayList<>();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(findAllByTitleEnContains + pageable);
            preparedStatement.setString(1, '%'+search+'%');
            ResultSet resultSet=preparedStatement.executeQuery();
            initSessionList(resultSet, sessions);
        } catch (SQLException e) {
            logger.error("Can't find session",e);
            throw new RepositoryException("Can't find session");
        }
        return sessions;
    }

    private final static String findAllByFilmTitleEnContainsAndWillBeShown =
            "SELECT * FROM \"session\" s INNER JOIN film f  ON s.film_id = f.film_id WHERE upper(f.title_en) LIKE ?" +
                    "AND ((s.date>current_date) OR (s.date=current_date AND s.time>=?)) ";
    public List<Session> findAllByFilmTitleEnContainsAndWillBeShown(LocalTime current_time, String search, Pageable pageable) {
        ArrayList<Session> sessions = new ArrayList<>();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(findAllByFilmTitleEnContainsAndWillBeShown + pageable);
            preparedStatement.setString(1, '%'+search+'%');
            preparedStatement.setTime(2, Time.valueOf(current_time));
            ResultSet resultSet=preparedStatement.executeQuery();
            initSessionList(resultSet, sessions);
        } catch (SQLException e) {
            logger.error("Can't find session",e);
            throw new RepositoryException("Can't find session");
        }
        return sessions;
    }

    private final static String findAllByFilmTitleEnContainsAndPast =
            "SELECT * FROM \"session\" s INNER JOIN film f  ON s.film_id = f.film_id WHERE upper(f.title_en) LIKE ?" +
                    "AND ((s.date<current_date) OR (s.date=current_date AND s.time<?)) ";
    public List<Session> findAllByFilmTitleEnContainsAndPast(LocalTime current_time, String search, Pageable pageable) {
        ArrayList<Session> sessions = new ArrayList<>();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(findAllByFilmTitleEnContainsAndPast + pageable);
            preparedStatement.setString(1, '%'+search+'%');
            preparedStatement.setTime(2, Time.valueOf(current_time));
            ResultSet resultSet=preparedStatement.executeQuery();
            initSessionList(resultSet, sessions);
        } catch (SQLException e) {
            logger.error("Can't find session",e);
            throw new RepositoryException("Can't find session");
        }
        return sessions;
    }

    private void initSessionList(ResultSet resultSet, List<Session> sessions) throws SQLException {
        while (resultSet.next()) {
            sessions.add(initSession(resultSet));
        }
    }

    private Session initSession(ResultSet resultSet) throws SQLException {
        Session session = new Session();
        session.setSession_id(resultSet.getLong("session_id")).
                setDate(resultSet.getDate("date").toLocalDate()).
                setTime(resultSet.getTime("time").toLocalTime()).
                setOccupancy(resultSet.getInt("occupancy")).
                setPrice(resultSet.getInt("price"));
        Long film_id = resultSet.getLong("film_id");
        Film film = new Film();
        film.setFilm_id(film_id);
        film.setBoxOffice(resultSet.getBoolean("box_office"));
        film.setDuration(Duration.ofNanos(resultSet.getLong("duration")));
        film.setTitleEn(resultSet.getString("title_en"));
        film.setTitleRu(resultSet.getString("title_ru"));
        session.setFilm(film);
        return session;
    }


    private final static String findAllBetween=
            "SELECT * FROM \"session\" s INNER JOIN film f  ON s.film_id = f.film_id " +
            "WHERE ((s.date>current_date ) OR (s.date=current_date AND s.time>=?))" +
            "AND s.film_id=? AND s.date=? AND s.occupancy<?";
    public List<Session> findAllBetween(Film film, LocalDate date1, LocalTime current, int occupancy, Sort sort) {
        ArrayList<Session> sessions = new ArrayList<>();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(findAllBetween + sort);
            preparedStatement.setTime(1, Time.valueOf(current));
            preparedStatement.setLong(2, film.getFilm_id());
            preparedStatement.setDate(3, Date.valueOf(date1));
            preparedStatement.setInt(4, occupancy);
            ResultSet resultSet=preparedStatement.executeQuery();
            initSessionList(resultSet, sessions);
        } catch (SQLException e) {
            logger.error("Can't find session",e);
            throw new RepositoryException("Can't find session");
        }
        return sessions;
    }

    private final static String findSessionCollision=
            "SELECT * FROM \"session\" s INNER JOIN film f ON s.film_id = f.film_id WHERE (s.date BETWEEN ? AND ?)";
    public List<Session> findSessionCollision(LocalDate date1, LocalDate date2) {
        ArrayList<Session> sessions = new ArrayList<>();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(findSessionCollision);
            preparedStatement.setDate(1, Date.valueOf(date1));
            preparedStatement.setDate(2, Date.valueOf(date2));
            ResultSet resultSet=preparedStatement.executeQuery();
            initSessionList(resultSet, sessions);
        } catch (SQLException e) {
            logger.error("Can't find film",e);
            throw new RepositoryException("Can't find film");
        }
        return sessions;
    }

    private final static String update =
            "UPDATE session SET occupancy =? WHERE session_id = ?";
    public void updateOccupancy(Long order_id, Integer occupancy) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(update);
            preparedStatement.setInt(1, occupancy);
            preparedStatement.setLong(2, order_id );
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Problem to create order",e);
            throw new RepositoryException("Problem to create order");
        }
    }

    private final static String save ="INSERT INTO \"session\" (\"date\" , occupancy, price, \"time\", film_id)" +
            "  VALUES (?, ?, ?, ?, ?) RETURNING session_id";
    public Session save(Session entity) {
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(save);
            preparedStatement.setDate(1, Date.valueOf(entity.getDate()));
            preparedStatement.setLong(2, entity.getOccupancy());
            preparedStatement.setLong(3, entity.getPrice());
            preparedStatement.setTime(4, Time.valueOf(entity.getTime()));
            preparedStatement.setLong(5, entity.getFilm().getFilm_id());
            ResultSet resultSet=preparedStatement.executeQuery();
            resultSet.next();
            entity.setSession_id(resultSet.getLong("session_id"));
        } catch (SQLException e) {
            logger.error("Can't save film", e);
            throw new RepositoryException("Can't save session");
        }
        return entity;
    }

    private final static String findById =
            " SELECT * FROM \"session\" s INNER JOIN film f  ON s.session_id = ? AND s.film_id = f.film_id ";
    public Optional<Session> findById(Long aLong) {
        Optional<Session> session = Optional.empty();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(findById);
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next())
              session=Optional.of(initSession(resultSet));
        } catch (SQLException e) {
            logger.error("Can't find session",e);
            throw new RepositoryException();
        }
        return session;
    }


    private final static String deleteById =
            "DELETE FROM \"session\" s WHERE s.session_id = ?";
    public void deleteById(Long aLong) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteById);
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete film", e);
            throw new RepositoryException("Can't delete film");
        }
    }

    public SessionRepository() {connection = ConnectionPool.getInstance().getConnection();}
    private SessionRepository(Connection connection) {
        this.connection = connection;
    }
    public static SessionRepository bound(Dao dao){
        return new SessionRepository(dao.connection);
    };
}
