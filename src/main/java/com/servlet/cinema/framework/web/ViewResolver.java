package com.servlet.cinema.framework.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ViewResolver {
    static void processView(String view, Model model, RedirectAttributes rA) throws ServletException, IOException {
        if (view.equals("HttpStatus.bad"))
            model.response.setStatus(400);
        if (!view.equals("HttpStatus.ok"))
            if (view.startsWith("redirect:")) {
                String link = model.request.getContextPath() + view.substring(9);
                model.request.getSession().setAttribute("flashAttributes", rA.flashAttributes);
                model.request.getSession().setAttribute("flashAttributesLink", link);
                String attributes = rA.merge();
                model.response.sendRedirect(link + attributes);
            } else {
                model.merge();
                view = "/WEB-INF/templates/" + view;
                RequestDispatcher dispatcher = model.request.getRequestDispatcher(view);
                dispatcher.forward(model.request, model.response);
            }
    }
}
