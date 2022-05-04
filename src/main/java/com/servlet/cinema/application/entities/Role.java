package com.servlet.cinema.application.entities;


import com.servlet.cinema.framework.security.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
