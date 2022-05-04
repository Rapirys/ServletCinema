package com.servlet.cinema.application.entities;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class Session {

    private Long session_id;


    private Film film;

    private LocalDate date;
    private LocalTime time;

    private int price;

    private int occupancy=0;


    public Session(){}

    public Session(Session session) {
        this.price=session.price;
        this.time=session.time;
        this.date=session.date;
        this.film=session.film;
        this.session_id=session.session_id;
        this.occupancy=session.occupancy;
    }

    public Film getFilm() {
        return film;
    }

    public Session setFilm(Film film) {
        this.film = film;
        return this;
    }

    public LocalDateTime getLocalDateTime(){
        return LocalDateTime.of(date, time);
    }

    public boolean isPassed(){
        return getLocalDateTime().plus(film.getDuration()).isBefore(LocalDateTime.now());
    }
    public boolean willBeShown(){
       return getLocalDateTime().isAfter(LocalDateTime.now());
    }
    public boolean isOnNow(){
       return !(isPassed() || willBeShown());
    }
    public Long getSession_id() {
        return session_id;
    }

    public Session setSession_id(Long session_id) {
        this.session_id = session_id;
        return this;
    }

    public LocalDate getDate() {
        return date;
    }

    public Session setDate(LocalDate date) {
        this.date = date;
        return this;
    }

    public LocalTime getTime() {
        return time;
    }

    public Session setTime(LocalTime time) {
        this.time = time;
        return this;
    }

    public int getPrice() {
        return price;
    }

    public Session setPrice(int price) {
        this.price = price;
        return this;
    }

    public int getOccupancy() {return occupancy;}

    public Session setOccupancy(int occupancy) {this.occupancy = occupancy; return this;   }

    public LocalTime getEndTime() {
        return time.plus(film.getDuration());
    }

    public void incOccupancy(int n) {
        occupancy=occupancy+n;
    }
}
