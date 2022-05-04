package com.servlet.cinema.framework.web;

import com.servlet.cinema.application.ServletCinemaApplication;
import com.servlet.cinema.framework.Util.AppContext;
import com.servlet.cinema.framework.Util.Pair;
import com.servlet.cinema.framework.annotation.Controller;
import com.servlet.cinema.framework.annotation.GetMapping;
import com.servlet.cinema.framework.annotation.PostMapping;
import com.servlet.cinema.framework.exaptions.ControllerNotExist;
import org.apache.log4j.Logger;
import org.reflections.Reflections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//Синглтон
public class HandlerMapping {
    private final static Logger logger = Logger.getLogger(HandlerMapping.class);
    private Map<String, Pair<Method, Object>> getRequests = new HashMap<>();
    private Map<String, Pair<Method, Object>> postRequests = new HashMap<>();
    private static HandlerMapping map = new HandlerMapping();

    public static HandlerMapping getInstance() {
        return map;
    }


    private HandlerMapping() {
        logger.debug("Beginning method assembly");
        Reflections reflections = new Reflections(AppContext.appClass.getPackage().getName());
        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        logger.debug("Detected controllers:" + controllers.toString() );
        initGet(controllers);
        initPost(controllers);

    }

    public Map<String, Pair<Method, Object>> getGetRequests() {
        return getRequests;
    }

    public Map<String, Pair<Method, Object>> getPostRequests() {
        return postRequests;
    }

    private void initGet(Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            Reflections reflectCont = new Reflections(controller.getPackage().getName());
            Method[] methods = controller.getMethods();
            try {
                int i=0;
                for (Method method : methods) {
                    if (method.isAnnotationPresent(GetMapping.class))
                        getRequests.put(method.getAnnotation(GetMapping.class).path(),
                                new Pair<Method, Object>(method, controller.getDeclaredConstructor().newInstance()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initPost(Set<Class<?>> controllers) {
        for (Class<?> controller : controllers) {
            Reflections reflectCont = new Reflections(controller.getPackage().getName());
            Method[] methods = controller.getMethods();
            try {
                for (Method method : methods) {
                    if (method.isAnnotationPresent(PostMapping.class))
                        postRequests.put(method.getAnnotation(PostMapping.class).path(),
                                new Pair<Method, Object>(method, controller.getDeclaredConstructor().newInstance()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Pair<Method,Object> getGet(String path){
        if (!getRequests.containsKey(path)) {
            logger.warn("Controller for path: "+ path +" not Exist");
            throw new ControllerNotExist("Controller fo path: "+ path +" not Exist");
        }
        return getRequests.get(path);
    }
    public Pair<Method,Object> getGet(HttpServletRequest request){
        String path=request.getRequestURI();
        if (!getRequests.containsKey(path)) {
            logger.warn("Controller for path: "+ path +" not Exist");
            throw new ControllerNotExist("Controller fo path: "+ path +" not Exist");
        }
        return getRequests.get(path);
    }
    public Pair<Method,Object> getPost(String path){
        if (!postRequests.containsKey(path)) {
            logger.error("Controller for path: "+ path +" not Exist");
            throw new ControllerNotExist("Controller for path: "+ path +" not Exist");
        }
        return postRequests.get(path);
    }
    public Pair<Method,Object> getPost(HttpServletRequest request) throws ControllerNotExist {
        String path=request.getRequestURI();
        if (!postRequests.containsKey(path)) {
            logger.error("Controller fo path: "+ path +" not Exist");
            throw new ControllerNotExist("Controller fo path: "+ path +" not Exist");
        }
        return postRequests.get(path);
    }

}
