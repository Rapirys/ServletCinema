package com.servlet.cinema.framework.security;

import com.servlet.cinema.application.entities.Role;
import com.servlet.cinema.application.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.util.Set;

import static javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE;
import static javax.servlet.jsp.tagext.Tag.SKIP_BODY;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecurityTagTest {

    @Test
    public void testDoStartTag() throws JspException {
        HttpSession session = mock(HttpSession.class);
        PageContext pc = mock(PageContext.class);
        when(pc.getSession()).thenReturn(session);

        SecurityTag st1 = new SecurityTag();
        st1.setPageContext(pc);

        st1.setAuthority("Authenticated");
        when(session.getAttribute("user")).thenReturn(new User());
        Assertions.assertEquals(st1.doStartTag(), EVAL_BODY_INCLUDE);

        st1.setAuthority("Authenticated");
        when(session.getAttribute("user")).thenReturn(null);
        Assertions.assertEquals(st1.doStartTag(), SKIP_BODY);

        st1.setAuthority("Anonymous");
        when(session.getAttribute("user")).thenReturn(new User());
        Assertions.assertEquals(st1.doStartTag(), SKIP_BODY);

        User user = new User();
        user.setRoles(Set.of(Role.USER));
        st1.setAuthority("USER");
        when(session.getAttribute("user")).thenReturn(user);
        Assertions.assertEquals(st1.doStartTag(), EVAL_BODY_INCLUDE);

        st1.setAuthority("ADMIN");
        when(session.getAttribute("user")).thenReturn(user);
        Assertions.assertEquals(st1.doStartTag(), SKIP_BODY);
    }
}