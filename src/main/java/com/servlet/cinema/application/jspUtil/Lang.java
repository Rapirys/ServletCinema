package com.servlet.cinema.application.jspUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.servlet.cinema.application.model.service.CookieManager.findCookiesByName;

public class Lang {
    HttpServletRequest request;
    ResourceBundle resourceBundle;

    public Lang(HttpServletRequest request) {
        this.request = request;
        Cookie cookie = findCookiesByName("lang", request);
        String lang;
        if (cookie == null) {
            lang = "en";
        } else lang = cookie.getValue();
        Locale locale = new Locale(lang);
        this.resourceBundle = ResourceBundle.getBundle("messages", locale);
    }

    public String gL(String message) {
        if (resourceBundle.containsKey(message))
            return resourceBundle.getString(message);
        else return message;
    }
}
