package com.servlet.cinema.framework.Util;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Function;

public class StandardConverter {
    Function<String, Integer> integerC = Integer::valueOf;
    Function<String, Boolean> booleanC = Boolean::valueOf;
    Function<String, Long> longC = Long::valueOf;
    Function<String, Double> doubleC = Double::valueOf;
    Function<String, String> stringC = x -> x;
    Function<String, LocalDate> localDateC = LocalDate::parse;
    Function<String, LocalTime> localTimeC = LocalTime::parse;
    Function<String, LocalDateTime> localDateTimeC = LocalDateTime::parse;
    Function<String, Duration> durationC = x-> Duration.between(LocalTime.MIN, LocalTime.parse(x));
}
