package com.servlet.cinema.application.model.service;


import com.servlet.cinema.framework.Util.AppContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@WebServlet("/upload/*")
public class ResourceHandler extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filename = request.getPathInfo();
        File file = new File(AppContext.property.getProperty("upload.path"), filename);


        response.setContentType(getServletContext().getMimeType(filename));
        response.setHeader("Content-Length", String.valueOf(file.length()));

        Files.copy(file.toPath(), response.getOutputStream());
    }

}
