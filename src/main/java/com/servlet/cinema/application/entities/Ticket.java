package com.servlet.cinema.application.entities;



import java.util.Objects;
import java.util.Random;


public class Ticket {

    private Long ticket_id;

    private Long order_id;
    private Long session_id;
    private int  place;
    private int row;
    private Long salt;

    public Ticket(int row, int place, Long order_id, Long session_id) {
        this.place = place;
        this.row = row;
        this.order_id =order_id;
        this.session_id =session_id;
        this.salt=new Random().nextLong();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return ticket_id.equals(ticket.ticket_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticket_id, place, row);
    }

    public Long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Long order_id) {
        this.order_id = order_id;
    }


    public Ticket() {
    }

    public Long getSession_id() {
        return session_id;
    }

    public void setSession_id(Long session_id) {
        this.session_id = session_id;
    }

    public Long getSalt() {
        return salt;
    }

    public void setSalt(Long salt) {
        this.salt = salt;
    }

    public Long getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(Long ticket_id) {
        this.ticket_id = ticket_id;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
