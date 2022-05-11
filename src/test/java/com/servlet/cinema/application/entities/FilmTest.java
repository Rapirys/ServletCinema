package com.servlet.cinema.application.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmTest {
    @Test
    void getTitleLocale() {
        Film film = new Film();
        film.setTitleEn("en");
        film.setTitleRu("ru");
        assertEquals(film.getTitleLocale("en"), "en");
        assertEquals(film.getTitleLocale("ru"), "ru");
    }
}