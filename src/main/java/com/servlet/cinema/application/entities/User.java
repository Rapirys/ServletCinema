package com.servlet.cinema.application.entities;


import com.servlet.cinema.framework.security.GrantedAuthority;
import com.servlet.cinema.framework.security.UserDetails;

import java.util.Collection;
import java.util.Set;


public class User implements UserDetails {

    private Integer id;
    private String username;
    private String email;
    private boolean active = true;
    String password;

    private Set<Role> roles;


    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(String name, String email, String password) {
        this.password = password;
        this.email = email;
        this.username = name;
    }

    public User(Integer id, String username, String email, boolean active, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.active = active;

        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public User setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }


    public User setUsername(String name) {
        this.username = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public User setActive(boolean active) {
        this.active = active;
        return this;
    }


    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public User setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public boolean hasRole(String roleStr) {
        Role role;
        try {
            role = Role.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return roles.contains(role);
    }
}