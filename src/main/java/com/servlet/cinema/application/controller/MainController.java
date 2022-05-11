package com.servlet.cinema.application.controller;


import com.servlet.cinema.application.entities.Film;
import com.servlet.cinema.application.entities.Session;
import com.servlet.cinema.application.model.service.CookieManager;
import com.servlet.cinema.application.model.service.SortManager;
import com.servlet.cinema.application.model.service.Validator;
import com.servlet.cinema.framework.annotation.Controller;
import com.servlet.cinema.framework.annotation.GetMapping;
import com.servlet.cinema.framework.annotation.RequestParam;
import com.servlet.cinema.framework.web.Model;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Controller
public class MainController {
    private final static Logger logger = Logger.getLogger(MainController.class);

    SortManager sortManager = SortManager.getInstance();

    @GetMapping(path = "/cinema")
    public String main(Model model,
                       @RequestParam(name = "search", defaultValue = "") String search,
                       @RequestParam(name = "availability", defaultValue = "false") Boolean availability,
                       @RequestParam(name = "session", defaultValue = "time") String sort_session,
                       @RequestParam(name = "films", defaultValue = "title_en") String sort_film,
                       @RequestParam(name = "date1", required = false) LocalDate date1,
                       @RequestParam(name = "date2", required = false) LocalDate date2) {
        LinkedList<Film> films = sortManager.findSimpleFilms(search, sort_film);
        System.out.println(sort_session);
        date1 = Validator.toValidDate(date1, 0);
        date2 = Validator.toValidDate(date2, 7);
        if (date2.isBefore(date1))
            date2 = date1;
        if (date1.plusDays(14).isBefore(date2))
            date2 = date1.plusDays(14);
        HashMap<Film, List<List<Session>>> sessions = sortManager.tableSessionByFilm(films, sort_session, date1, date2, availability);
        if (films.size() == 1)
            films.add(films.get(0));
        if (films.size() == 0)
            return "redirect:/cinema/zero_films";
        model.addAttribute("sessions", sessions);
        model.addAttribute("films", films);
        return "main.jsp";
    }

    @GetMapping(path = "/cinema/zero_films")
    public String zero() {
        return "zero_films.jsp";
    }

    @GetMapping(path = "/cinema/command/lang")
    public String lang(HttpServletRequest request, HttpServletResponse response) {
        CookieManager.changLang(request, response);
        return "HttpStatus.ok";
    }

}