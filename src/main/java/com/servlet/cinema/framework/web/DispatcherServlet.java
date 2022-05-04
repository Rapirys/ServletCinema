package com.servlet.cinema.framework.web;


import com.servlet.cinema.framework.Util.Pair;
import com.servlet.cinema.framework.annotation.RequestParam;

import com.servlet.cinema.framework.exaptions.ControllerNotExist;
import com.servlet.cinema.framework.exaptions.NullParamException;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;


import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.servlet.cinema.framework.Util.Converter.convert;
import static com.servlet.cinema.framework.web.ViewResolver.processView;


@WebServlet("/cinema/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 , // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class DispatcherServlet extends HttpServlet{
    private final static Logger logger = Logger.getLogger(DispatcherServlet.class);
    private final HandlerMapping  handlerMapping = HandlerMapping.getInstance();

    public void init(){

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path=request.getRequestURI();
        Pair<Method,Object> pair= null;
        pair = handlerMapping.getGet(path);
        Model model=new Model(request, response);
        RedirectAttributes rA = new RedirectAttributes(model);
        String view = doRequest(pair, model, rA);
        processView(view, model, rA);

    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path=request.getRequestURI();
        Pair<Method,Object> pair= null;
        pair = handlerMapping.getPost(path);
        Model model=new Model(request, response);
        RedirectAttributes rA = new RedirectAttributes(model);
        String view = doRequest(pair, model, rA);
        processView(view, model, rA);
    }
    private String doRequest(Pair<Method, Object> pair, Model model, RedirectAttributes rA) {
        Method method = pair.getFirst();
        Object controller= pair.getSecond();
        Object[] parameters= injectParameters(method, model, rA);
        try {
           return (String) method.invoke(controller,parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Cant invoke request to controller: "+ pair.getSecond(), e);
            return "error";
        } catch (IllegalArgumentException e){
            logger.error("Problem with method invoke", e);
            return "error";
        }
    }

    private Object[] injectParameters(Method method, Model model, RedirectAttributes rA) {
        List<Object> result= new ArrayList<>();
        Parameter[] parameter = method.getParameters();
        for (Parameter p:parameter){
            if (p.isAnnotationPresent(RequestParam.class)){
                RequestParam rp = p.getAnnotation(RequestParam.class);
                String name = rp.name();
                if (p.getType().isArray()){
                    String[] data = model.request.getParameterValues(name);
                    data = checkDefaultAndRequiredForArr(rp, data);
                    result.add(convert(data,p.getType()));
                }else {
                    String data = model.request.getParameter(name);
                    data = checkDefaultAndRequired(rp, data);
                    result.add(convert(data,p.getType()));
                }
            }
            if (p.getType().equals(Model.class))
                result.add(model);
            else if (p.getType().equals(RedirectAttributes.class))
                result.add(rA);
            else if (p.getType().equals(HttpServletRequest.class))
                result.add(model.request);
            else if (p.getType().equals(HttpServletResponse.class))
                result.add(model.response);
        }
        return result.toArray();
    }
    private String checkDefaultAndRequired(RequestParam rp, String data) {
        if (rp.required() && rp.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n") && data == null) {
            logger.error("parameter " + rp.name() + " not found");
            throw new NullParamException("parameter " + rp.name() + " not found");
        }
        if (!rp.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n") && data == null)
            data = rp.defaultValue();
        return data;
    }
    private String[] checkDefaultAndRequiredForArr(RequestParam rp, String[] data) {
        if (data == null) {
            if (rp.required() && rp.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n")){
                logger.error("parameter " + rp.name() + " not found");
                throw new NullParamException("parameter " + rp.name() + " not found");
            }
            if (!rp.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n"))
                data = new String[]{rp.defaultValue()};
            else data = new String[]{};
        }
        return data;
    }

    public void destroy() {
    }
}