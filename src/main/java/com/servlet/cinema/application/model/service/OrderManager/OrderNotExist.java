package com.servlet.cinema.application.model.service.OrderManager;

public class OrderNotExist extends Exception {
    public OrderNotExist(String message) {
        super(message);
    }

    public OrderNotExist() {
    }
}
