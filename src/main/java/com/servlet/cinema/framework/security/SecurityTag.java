package com.servlet.cinema.framework.security;


import com.servlet.cinema.application.entities.User;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

public class SecurityTag extends TagSupport {

    private String authority;

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public int doStartTag(){
        HttpSession session = pageContext.getSession();
        User user = SecurityManager.getUserFromSession(session);
        if ((user == null && authority.equals("Anonymous")) ||
                (user != null && authority.equals("Authenticated")) ||
                (user != null && user.hasRole(authority)))
            return EVAL_BODY_INCLUDE;
        else return SKIP_BODY;
    }

}