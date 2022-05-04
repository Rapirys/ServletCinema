package com.servlet.cinema.framework.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class Model {
    public Map <String, Object> attributes;
    public HttpServletRequest request;
    public HttpServletResponse response;

    public Model(HttpServletRequest request, HttpServletResponse response) {
        this.attributes = new HashMap<String, Object>();
        this.request = request;
        this.response = response;
    }

    public Object getAttribute(String name) {
        if (attributes.containsKey(name))
           return attributes.get(name);
        return request.getParameter(name);
    }

    public void addAttribute(String name, Object o) {
        attributes.put(name, o);
    }

    public void merge(){
        for(Map.Entry<String, Object> entry: attributes.entrySet()){
            request.setAttribute(entry.getKey(), entry.getValue());
        }
    }

    public boolean containsAttribute(String attribute) {
        return attributes.containsKey(attribute);
    }
}
