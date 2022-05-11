package com.servlet.cinema.framework.exaptions;

public class ControllerNotExist extends RuntimeException {
    public ControllerNotExist() {
        super();
    }

    public ControllerNotExist(String s) {
        super(s);
    }
}

