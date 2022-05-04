package com.servlet.cinema.application.model.service.Hall;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a seats in a movie theater.
 * Contains data about the seat number and its type.
 * '0'-no space.
 * '#'-free(default).
 * 'X'- is busy.
 */
public class Place implements Serializable {
    public int row, place;
    public Character type;

    public Place(){

    }
    public Place(int row, int place, Character type) {
        this.type = type;
        if (type == '#') {
            this.row = row;
            this.place = place;
        }
    }
    public Place(Place place) {
        this.type = place.type;
        if (type == '#') {
            this.row = place.row;
            this.place = place.place;
        }
    }
    public void setType(Character type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place)) return false;
        Place place1 = (Place) o;
        return row == place1.row && place == place1.place && type.equals(place1.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, place, type);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public Character getType() {
        return type;
    }
}
