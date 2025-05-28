package com.prathmesh.New_project.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectURL = request.getContextPath();

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                redirectURL += "/employees/list-employee";
                break;
            } else if (role.equals("ROLE_MANAGER")) {
                redirectURL += "/employees/list-employee";
                break;
            } else if (role.equals("ROLE_EMPLOYEE")) {
                redirectURL += "/employees/list-employee";
                break;
            }
        }

        response.sendRedirect(redirectURL);
    }
}
