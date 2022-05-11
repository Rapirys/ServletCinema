package com.servlet.cinema.application.model.repository;

import com.servlet.cinema.application.entities.Film;
import com.servlet.cinema.framework.data.ConnectionPool;
import com.servlet.cinema.framework.data.Dao;
import com.servlet.cinema.framework.data.Pageable;
import com.servlet.cinema.framework.data.Sort;
import com.servlet.cinema.framework.exaptions.ConnectionPoolException;
import com.servlet.cinema.framework.exaptions.RepositoryException;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


public class FilmRepository extends Dao {
    private final static Logger logger = Logger.getLogger(FilmRepository.class);


    private final static String findAllByTitleEnContainsOrTitleRuContains =
            "SELECT * FROM film f WHERE UPPER(f.title_en) LIKE UPPER(?) OR UPPER(f.title_ru) LIKE UPPER(?)";

    public LinkedList<Film> findAllByTitleEnContainsOrTitleRuContains(String titleEn, String titleRu, Sort sort) {
        LinkedList<Film> filmSet = new LinkedList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(findAllByTitleEnContainsOrTitleRuContains + sort);
            preparedStatement.setString(1, '%' + titleEn + '%');
            preparedStatement.setString(2, '%' + titleRu + '%');
            ResultSet resultSet = preparedStatement.executeQuery();
            initFilmList(resultSet, filmSet);
        } catch (SQLException e) {
            throw new RepositoryException("Can't find film", e);
        }
        return filmSet;
    }

    public List<Film> findAllByTitleEnContainsOrTitleRuContains(String titleEn, String titleRu, Pageable pageable) {
        ArrayList<Film> filmSet = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(findAllByTitleEnContainsOrTitleRuContains + pageable);
            preparedStatement.setString(1, '%' + titleEn + '%');
            preparedStatement.setString(2, '%' + titleRu + '%');
            ResultSet resultSet = preparedStatement.executeQuery();
            initFilmList(resultSet, filmSet);
        } catch (SQLException e) {
            logger.error("Can't find film");
            throw new RepositoryException("Can't find film", e);
        }
        return filmSet;
    }

    private final static String findAllByTitleEnContainsOrTitleRuContainsAndBoxOffice =
            "SELECT * FROM film f WHERE UPPER(f.title_en) LIKE UPPER(?) OR UPPER(f.title_ru) LIKE UPPER(?) AND f.box_office =?";

    public List<Film> findAllByTitleEnContainsOrTitleRuContainsAndBoxOffice(String search_ru, String search_en, boolean box_office, Pageable pageable) {
        ArrayList<Film> filmSet = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(findAllByTitleEnContainsOrTitleRuContainsAndBoxOffice + pageable);
            preparedStatement.setString(1, '%' + search_en + '%');
            preparedStatement.setString(2, '%' + search_ru + '%');
            preparedStatement.setBoolean(3, box_office);
            ResultSet resultSet = preparedStatement.executeQuery();
            initFilmList(resultSet, filmSet);
        } catch (SQLException e) {
            logger.error("Can't find film");
            throw new RepositoryException("Can't find film", e);
        }
        return filmSet;
    }





    private final static String findByBoxOfficeTrueOrderByTitleEn =
            "SELECT * FROM film f WHERE f.box_office = true ORDER BY f.title_en";

    public List<Film> findByBoxOfficeTrueOrderByTitleEn() {
        ArrayList<Film> filmSet = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(findByBoxOfficeTrueOrderByTitleEn);
            ResultSet resultSet = preparedStatement.executeQuery();
            initFilmList(resultSet, filmSet);
        } catch (SQLException e) {
            logger.error("Can't find film");
            throw new RepositoryException("Can't find film", e);
        }
        return filmSet;
    }

    private void initFilmList(ResultSet resultSet, List<Film> filmSet) throws SQLException {
        while (resultSet.next())
            filmSet.add(initFilm(resultSet));
    }

    private final static String update_status = "UPDATE Film SET box_office=? WHERE film_id = ?";

    public void update_status(Long id, boolean b) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(update_status);
            preparedStatement.setBoolean(1, b);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Problem to update status for film");
            throw new ConnectionPoolException("Problem to update status for film", e);
        }
    }

    private final static String save =
            "INSERT INTO film (box_office,duration,title_en,title_ru) VALUES (?, ?, ?, ?) RETURNING film_id";

    public Film save(Film entity) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(save);
            preparedStatement.setBoolean(1, entity.isBoxOffice());
            preparedStatement.setLong(2, entity.getDuration().toNanos());
            preparedStatement.setString(3, entity.getTitleEn());
            preparedStatement.setString(4, entity.getTitleRu());
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            entity.setFilm_id(resultSet.getLong("film_id"));
        } catch (SQLException e) {
            logger.error("Problem to insert into film");
            throw new ConnectionPoolException("Problem to insert into film", e);
        }
        return entity;
    }


    private final static String findById =
            "SELECT * FROM film f WHERE f.film_id = ?";

    public Optional<Film> findById(Long aLong) {
        Optional<Film> filmO = Optional.empty();
        try {
            Film film;
            PreparedStatement preparedStatement = connection.prepareStatement(findById);
            preparedStatement.setLong(1, aLong);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                filmO = Optional.of(initFilm(resultSet));
        } catch (SQLException e) {
            logger.error("Can't find film");
            throw new RepositoryException("Can't find film", e);
        }
        return filmO;
    }

    private Film initFilm(ResultSet resultSet) throws SQLException {
        Film film;
        film = new Film();
        film.setFilm_id(resultSet.getLong(1));
        film.setBoxOffice(resultSet.getBoolean(2));
        film.setDuration(Duration.ofNanos(resultSet.getLong(3)));
        film.setTitleEn(resultSet.getString(4));
        film.setTitleRu(resultSet.getString(5));
        return film;
    }

    private final static String deleteById =
            "DELETE FROM film f WHERE f.film_id = ?";

    public void deleteById(Long aLong) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(deleteById);
            preparedStatement.setLong(1, aLong);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete film");
            throw new RepositoryException("Can't delete film", e);
        }
    }

    public FilmRepository() {
        connection = ConnectionPool.getInstance().getConnection();
    }

    private FilmRepository(Connection connection) {
        this.connection = connection;
    }

    public static FilmRepository bound(Dao dao) {
        return new FilmRepository(dao.connection);
    }

}
