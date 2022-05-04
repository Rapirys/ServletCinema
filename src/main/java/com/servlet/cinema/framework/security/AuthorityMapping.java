package com.servlet.cinema.framework.security;

import com.servlet.cinema.application.entities.Role;
import com.servlet.cinema.framework.Util.AppContext;
import com.servlet.cinema.framework.Util.Pair;
import com.servlet.cinema.framework.annotation.Controller;
import com.servlet.cinema.framework.annotation.GetMapping;
import com.servlet.cinema.framework.annotation.PreAuthorize;
import com.servlet.cinema.framework.exaptions.ControllerNotExist;
import com.servlet.cinema.framework.web.HandlerMapping;
import org.apache.log4j.Logger;
import org.reflections.Reflections;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.*;

public class AuthorityMapping {
    private final static Logger logger = Logger.getLogger(AuthorityMapping.class);
    private Map<String, Set<String>> getAuthorities = new HashMap<>();
    private Map<String, Set<String>> postAuthorities = new HashMap<>();

    private static AuthorityMapping map = new AuthorityMapping();

    public static AuthorityMapping getInstance() {
        return map;
    }

    private AuthorityMapping() {
        logger.debug("Beginning security mapping");
//        Reflections reflections = new Reflections(AppContext.appClass.getPackage().getName());
//        Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
        HandlerMapping handlerMapping = HandlerMapping.getInstance();
        initGet(handlerMapping.getGetRequests());
        initPost(handlerMapping.getPostRequests());
    }
    private void initGet(Map<String, Pair<Method, Object>> getRequests) {
        for (Map.Entry<String, Pair<Method, Object>> entry : getRequests.entrySet()) {
            Set<String> roles =new HashSet<String>();
            if (entry.getValue().getFirst().isAnnotationPresent(PreAuthorize.class))
                roles.addAll(
                        Arrays.asList(entry.getValue().getFirst().getAnnotation(PreAuthorize.class).value()
                                .split("\\Q||"))
                );
            getAuthorities.put(entry.getKey(),roles);
        }
    }
    private void initPost(Map<String, Pair<Method, Object>> postRequests) {
        for (Map.Entry<String, Pair<Method, Object>> entry : postRequests.entrySet()) {
            Set<String> roles =new HashSet<String>();
            if (entry.getValue().getFirst().isAnnotationPresent(PreAuthorize.class))
                roles.addAll(
                        Arrays.asList(entry.getValue().getFirst().getAnnotation(PreAuthorize.class).value()
                                .split("\\Q||"))
                );
            postAuthorities.put(entry.getKey(),roles);
        }
    }

    public Set<String> getGet(String path){
        if (getAuthorities.containsKey(path)) {
            return getAuthorities.get(path);
        }else return new HashSet<String>();
    }

    public Set<String> getPost(String path){
        if (postAuthorities.containsKey(path)) {
            return postAuthorities.get(path);
        }else return new HashSet<String>();
    }


}
