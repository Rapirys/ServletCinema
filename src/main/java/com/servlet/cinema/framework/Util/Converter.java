package com.servlet.cinema.framework.Util;


import com.servlet.cinema.framework.exaptions.ConverterException;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.function.Function;

public class Converter {
    private final static Logger logger = Logger.getLogger(Converter.class);

    static HashMap<Class<?>, Function<String, ?>> register = new HashMap<>();

    static {
        StandardConverter sc = new StandardConverter();
        register.put(Integer.class, sc.integerC);
        register.put(Boolean.class, sc.booleanC);
        register.put(Long.class, sc.longC);
        register.put(Double.class, sc.doubleC);
        register.put(String.class, sc.stringC);
        register.put(Duration.class, sc.durationC);
        register.put(LocalDate.class, sc.localDateC);
        register.put(LocalDateTime.class, sc.localDateTimeC);
        register.put(LocalTime.class, sc.localTimeC);
    }

    public static Object convert(String p, Class<?> c) {
        if (p == null)
            return null;
        if (register.containsKey(c))
            return register.get(c).apply(p);
        logger.error("Cant convert String to " + c);
        throw new ConverterException("Cant convert String to " + c);
    }

    public static String[] convert(String[] parameters, Class<?> c) {
        return parameters;
    }
}
