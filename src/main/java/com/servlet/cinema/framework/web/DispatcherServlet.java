package com.servlet.cinema.framework.web;


import com.servlet.cinema.framework.Util.Pair;
import com.servlet.cinema.framework.annotation.RequestParam;
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
import java.util.ArrayList;
import java.util.List;

import static com.servlet.cinema.framework.Util.Converter.convert;
import static com.servlet.cinema.framework.web.ViewResolver.processView;

/**
 * The class DispatcherServlet process all requests for /main/*
 * It takes the path, search for corresponding controller, inject parameters
 * and invoke right method.
 */
@WebServlet("/cinema/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024,  // 1 MB
        maxFileSize = 1024 * 1024 * 10,    // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)
public class DispatcherServlet extends HttpServlet {
    private final static Logger logger = Logger.getLogger(DispatcherServlet.class);
    private final HandlerMapping handlerMapping = HandlerMapping.getInstance();

    public void init() {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI();
        Pair<Method, Object> pair = handlerMapping.getGet(path);
        doRequest(pair, request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getRequestURI();
        Pair<Method, Object> pair = handlerMapping.getPost(path);
        doRequest(pair, request, response);
    }

    /**
     * Process request, initializes the necessary objects,
     * inject a list of parameters, invoke the request processing method at controller
     * and passes on further processing to ViewResolver.
     *
     * @param pair     pair of responsible controller method and controller object to invoke this method
     * @param request  injected into servlet.
     * @param response injected into servlet.
     * @throws ServletException throws servlet exception when can't invoke responsible method
     * @see RequestParam
     */
    private void doRequest(Pair<Method, Object> pair, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Model model = new Model(request, response);
        RedirectAttributes rA = new RedirectAttributes(model);
        Method method = pair.getFirst();
        Object controller = pair.getSecond();
        Object[] parameters = injectParameters(method, model, rA);
        try {
            String view = (String) method.invoke(controller, parameters);
            processView(view, model, rA);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Cant invoke request to controller: " + pair.getSecond(), e);
            throw new ServletException(e);
        } catch (IllegalArgumentException e) {
            logger.error("Problem with method invoke", e);
            throw new ServletException(e);
        }
    }

    /**
     * responsible for mapping parameters in array
     * according to @RequestParam annotation for future method invocation
     *
     * @param method method in according which signature parameters will be mapping
     * @param model  contains information about request and response, maps into @RequestParam Model
     * @param rA     contains information about redirect ,maps into @RequestParam RedirectAttributes
     * @return returns array of parameters for according to method signature
     * @see RequestParam
     */
    private Object[] injectParameters(Method method, Model model, RedirectAttributes rA) {
        List<Object> result = new ArrayList<>();
        Parameter[] parameter = method.getParameters();
        for (Parameter p : parameter) {
            if (p.isAnnotationPresent(RequestParam.class)) {
                RequestParam rp = p.getAnnotation(RequestParam.class);
                String name = rp.name();
                if (p.getType().isArray()) {
                    String[] data = model.request.getParameterValues(name);
                    data = checkDefaultAndRequiredForArr(rp, data);
                    result.add(data);
                } else {
                    String data = model.request.getParameter(name);
                    data = checkDefaultAndRequired(rp, data);
                    result.add(convert(data, p.getType()));
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

    /**
     * @param rp   - RequestParam, object witch contains p
     * @param data - String representation of value of parameter
     * @return checks required and defaultValue constraints returns data or defaultValue or throws NullParamException
     * @see RequestParam
     */
    private String checkDefaultAndRequired(RequestParam rp, String data) {
        if (rp.required() && rp.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n") && data == null) {
            logger.error("parameter " + rp.name() + " not found");
            throw new NullParamException("parameter " + rp.name() + " not found");
        }
        if (!rp.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n") && data == null)
            data = rp.defaultValue();
        return data;
    }

    /**
     * @param rp   - RequestParam, object witch contains p
     * @param data - String array representation of value of parameter
     * @return checks required and defaultValue constraints returns data or defaultValue or throws NullParamException
     * @see RequestParam
     */
    private String[] checkDefaultAndRequiredForArr(RequestParam rp, String[] data) {
        if (data == null) {
            if (rp.required() && rp.defaultValue().equals("\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n")) {
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