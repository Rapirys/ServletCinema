package com.servlet.cinema.framework.web;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class that controllers can use to select attributes for a redirect scenario.
 */
public class RedirectAttributes {

    public Map<String, List<String>> attributes;
    public Map<String, Object> flashAttributes;


    public RedirectAttributes(Model model) {
        Map<String, Object> flashAttributes = (Map<String, Object>) model.request.getSession().getAttribute("flashAttributes");
        if (flashAttributes != null
                && model.request.getSession().getAttribute("flashAttributesLink").equals(model.request.getRequestURI()))
            model.attributes.putAll(flashAttributes);
        model.request.getSession().removeAttribute("flashAttributes");
        model.request.getSession().removeAttribute("flashAttributesLink");
        this.attributes = new HashMap<>();
        this.flashAttributes = new HashMap<>();
    }

    public RedirectAttributes() {
        this.attributes = new HashMap<>();
    }

    public List<String> getAttribute(String name) {
        if (attributes.containsKey(name))
            return attributes.get(name);
        return null;
    }

    public void addAttributes(String name, List<String> o) {
        attributes.put(name, o);
    }

    public void addAttribute(String name, String o) {
        attributes.put(name, new ArrayList(List.of(o)));
    }


    /**
     * @return Add attributes to redirect URL.
     */
    public String merge() {
        StringBuilder stringBuilder = new StringBuilder("?");
        for (Map.Entry<String, List<String>> entry : attributes.entrySet()) {
            for (String value : entry.getValue()) {
                stringBuilder.append(entry.getKey())
                        .append("=")
                        .append(value)
                        .append("&");
            }
        }
        return stringBuilder.toString();
    }

    public void addFlashAttribute(String name, Object o) {
        flashAttributes.put(name, o);
    }
}
