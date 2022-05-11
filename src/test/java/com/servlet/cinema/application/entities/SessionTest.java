package com.servlet.cinema.application.entities;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SessionTest {

    @Test
    void isPassed() {
        Session session = new Session();
        Film film = new Film();
        film.setDuration(Duration.ofHours(2));
        session.setFilm(film);

        session.setDate(LocalDate.now().plusDays(100));
        session.setTime(LocalTime.now());
        assertFalse(session.isPassed());

        session.setDate(LocalDate.now().minusDays(100));
        session.setTime(LocalTime.now());
        assertTrue(session.isPassed());
    }

    @Test
    void isWillCome() {
        Session session = new Session();

        session.setDate(LocalDate.now().plusDays(100));
        session.setTime(LocalTime.now());
        assertTrue(session.willBeShown());

        session.setDate(LocalDate.now().minusDays(100));
        session.setTime(LocalTime.now());
        assertFalse(session.willBeShown());
    }


    @Test
    void isNow() {
        Session session = new Session();
        Film film = new Film();
        film.setDuration(Duration.ofHours(2));
        session.setFilm(film);

        session.setDate(LocalDate.now());
        session.setTime(LocalTime.now().plusHours(1));
        assertFalse(session.isOnNow());
    }

}