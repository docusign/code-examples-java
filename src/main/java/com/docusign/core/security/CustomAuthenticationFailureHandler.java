package com.docusign.core.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        String code = request.getParameter("code");
        String state = request.getParameter("state");

        if (code != null) {
            response.sendRedirect("/pkce?code=" + code + "&state=" + state);
        } else {
            response.sendRedirect("/login?error=true");
        }
    }
}
