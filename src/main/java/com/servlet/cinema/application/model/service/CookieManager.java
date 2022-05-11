package com.servlet.cinema.application.model.service;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Used for easy access to cookies.
 */
public class CookieManager {

    public static Cookie findCookiesByName(String name, HttpServletRequest request) {
        if (request.getCookies() == null)
            return null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name))
                return cookie;
        }
        return null;
    }

    public static void changUniqueCookie(String name, String value, HttpServletRequest request, HttpServletResponse response) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(name))
                cookie.setMaxAge(0);
        }
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void changLang(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = findCookiesByName("lang", request);
        String lang;
        if (cookie == null) {
            lang = "ru";
            cookie = new Cookie("lang", lang);
            cookie.setPath("/");
            response.addCookie(cookie);
        } else {
            lang = cookie.getValue();
            if (lang.equals("en"))
                changUniqueCookie("lang", "ru", request, response);
            else if (lang.equals("ru"))
                changUniqueCookie("lang", "en", request, response);
        }
    }
}
