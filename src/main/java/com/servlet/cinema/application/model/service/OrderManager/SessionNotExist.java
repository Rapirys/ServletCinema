package com.servlet.cinema.application.model.service.OrderManager;

public class SessionNotExist extends Exception {
    public SessionNotExist(String message) {
        super(message);
    }

    public SessionNotExist() {
    }
}
