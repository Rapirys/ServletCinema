package com.servlet.cinema.framework.web;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class RedirectAttributesTest {

    @Test
    public void testMerge() {

        RedirectAttributes ra = new RedirectAttributes();
        Assertions.assertEquals("?", ra.merge());
        try {
            ra.addAttribute("test", null);
            Assertions.fail("NullPointerException");
        } catch (NullPointerException e) {
        }

        ra.addAttribute("test1", "1");
        List<String> attributes = new LinkedList<>();
        attributes.add("2");
        attributes.add("3");
        ra.addAttributes("test2", attributes);

        String lpString = ra.merge();
        String[] lp = lpString.substring(1, lpString.length() - 1).split("&");
        Arrays.sort(lp);

        Assertions.assertArrayEquals(new String[]{"test1=1", "test2=2", "test2=3"}, lp);
    }
}