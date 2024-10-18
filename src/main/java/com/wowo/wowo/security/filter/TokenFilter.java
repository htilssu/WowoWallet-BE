package com.wowo.wowo.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.wowo.wowo.services.JwtService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class TokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
                                                                                              ServletException,
                                                                                              IOException {


        final Cookie[] cookies = ((HttpServletRequest) request).getCookies();

        if (cookies == null) {
            chain.doFilter(request, response);
            return;
        }

        var cookie = Arrays.stream(cookies).filter(
                c -> c.getName().equals("Token")).findFirst().orElse(null);

        if (cookie == null) {
            chain.doFilter(request, response);
            return;
        }

        final DecodedJWT decodedJWT = JwtService.verifyToken(cookie.getValue());
        if (decodedJWT == null) {
            chain.doFilter(request, response);
            return;
        }

        final String role = decodedJWT.getClaim("role").as(String.class);

        SecurityContextHolderStrategy contextHolder =
                SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        var authorities = Collections.singleton(
                new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(), cookie.getValue(),
                        authorities);

        authenticationToken.setDetails(decodedJWT);

        context.setAuthentication(authenticationToken);
        contextHolder.setContext(context);
        chain.doFilter(request, response);
    }
}
