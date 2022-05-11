package com.servlet.cinema.application.controller;

import com.servlet.cinema.framework.annotation.Controller;
import com.servlet.cinema.framework.annotation.GetMapping;
import com.servlet.cinema.framework.annotation.PostMapping;
import com.servlet.cinema.framework.exaptions.ControllerNotExist;
import com.servlet.cinema.framework.web.Model;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


@Controller
public class MyErrorController {
    private final static Logger logger = Logger.getLogger(MyErrorController.class);

    @GetMapping(path = "/cinema/error")
    @PostMapping(path = "/cinema/error")
    public String handleError(HttpServletRequest request, Model model) {
        Throwable throwable = (Throwable)
                request.getAttribute("javax.servlet.error.exception");
        Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (throwable != null) {
            if (throwable.getClass().equals(ControllerNotExist.class))
                status = 404;
            else logger.error("New error", throwable);
        }
        if (status != null) {
            if (status.equals(404)) {
                model.addAttribute("message", "Page_not_found");
                model.addAttribute("error", "404");
                return "error.jsp";
            }
        }
        model.addAttribute("message", "Something_went_wrong");
        model.addAttribute("error", "Error");
        return "error.jsp";
    }


    @PostMapping(path = "/cinema/error403")
    @GetMapping(path = "/cinema/error403")
    public String handleError403() {
        return "error403.jsp";
    }
}

