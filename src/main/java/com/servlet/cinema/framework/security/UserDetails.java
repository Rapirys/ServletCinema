package com.servlet.cinema.framework.security;

import java.util.Collection;

public interface UserDetails {
    Collection<? extends GrantedAuthority> getAuthorities();
}
