package com.servlet.cinema.framework.Util;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static com.servlet.cinema.framework.Util.Converter.convert;
import static org.junit.jupiter.api.Assertions.*;


class ConverterTest {
    @Test
    public void convertFromString(){
        assertEquals(convert("123", Integer.class), 123);
        assertEquals(convert("123", Long.class), 123L);
        assertEquals(convert("12.3", Double.class), 12.3);
        assertEquals(convert("true", Boolean.class), true);
        assertEquals(convert("12:00", LocalTime.class), LocalTime.parse("12:00"));
    }

}