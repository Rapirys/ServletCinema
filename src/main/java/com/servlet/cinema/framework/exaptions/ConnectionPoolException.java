package com.servlet.cinema.framework.exaptions;

public class ConnectionPoolException extends RuntimeException{
    public ConnectionPoolException(){
        super();
    }
    public ConnectionPoolException(String s){
        super(s);
    }

    public ConnectionPoolException(String message, Throwable cause) {
        super(message, cause);
    }
}

