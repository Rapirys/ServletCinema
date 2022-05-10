package com.servlet.cinema.application.entities;

import com.servlet.cinema.application.entities.Film;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {
    @Test
    void getTitleLocale() {
        Film film = new Film();
        film.setTitleEn("en");
        film.setTitleRu("ru");
        assertEquals(film.getTitleLocale("en"),"en");
        assertEquals(film.getTitleLocale("ru"),"ru");
    }
}