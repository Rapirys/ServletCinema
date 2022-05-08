package com.servlet.cinema.framework.Util;


import com.servlet.cinema.framework.exaptions.ConverterException;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.function.Function;

/**
 * The class used to convert objects in the framework.
 * It registers "converters" that are used within the framework
 * and can also be used by external code.
 */
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

    /**
     * Converts strings in an object.
     * @param p the string to convert.
     * @param c the class of the object to convert the string to.
     * @return the object of class c corresponding to the string p.
     */
    public static Object convert(String p, Class<?> c) {
        if (p == null)
            return null;
        if (register.containsKey(c))
            return register.get(c).apply(p);
        logger.error("Cant convert String to " + c);
        throw new ConverterException("Cant convert String to " + c);
    }

}
