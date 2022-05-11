package com.servlet.cinema.framework.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import java.io.IOException;


/**
 * Class that can resolve views by name.
 */
public class ViewResolver {
    /**
     * Resolve the given view by name.
     *
     * @param view  - name of the view to resolve.
     * @param model - model for current request.
     * @param rA    - redirectAttributes for current request.
     */
    static void processView(String view, Model model, RedirectAttributes rA) throws ServletException, IOException {
        if (view.equals("HttpStatus.bad"))
            model.response.setStatus(400);
        if (!view.equals("HttpStatus.ok"))
            if (view.startsWith("redirect:")) {
                String link = view.substring(9);
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
