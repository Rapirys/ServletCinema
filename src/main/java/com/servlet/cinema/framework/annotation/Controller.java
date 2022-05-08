package com.servlet.cinema.framework.annotation;

import java.lang.annotation.*;

/**
 * Indicates that an annotated class is a "Controller" (e.g. a web controller).
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

}
