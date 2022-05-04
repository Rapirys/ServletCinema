package com.servlet.cinema.framework.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PostMapping {
    String path() default "";
}
