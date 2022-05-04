package com.servlet.cinema.framework.exaptions;

import java.sql.SQLException;

public class RepositoryException extends RuntimeException{
    public RepositoryException(){
        super();
    }
    public RepositoryException(String s){
        super(s);
    }
    public RepositoryException(Throwable e){
        super(e);
    }

    public RepositoryException(String s, SQLException e) {
        super(s,e);
    }
}