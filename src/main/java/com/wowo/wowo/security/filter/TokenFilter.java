package com.wowo.wowo.security.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.wowo.wowo.util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;

import java.io.IOException;
import java.util.Collections;

public class TokenFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws ServletException, IOException {


        var authorizationContent = setResponseHeader((HttpServletRequest) request,
                (HttpServletResponse) response
        );

        if (authorizationContent == null || !authorizationContent.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        var token = authorizationContent.split(" ")[1];

        if (token != null && !token.isEmpty()) {
            final DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
            if (decodedJWT == null) {

                chain.doFilter(request, response);

                return;
            }

            SecurityContextHolderStrategy contextHolder =
                    SecurityContextHolder.getContextHolderStrategy();
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            var authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            decodedJWT.getSubject(), token, authorities);

            authenticationToken.setDetails(decodedJWT.getSubject());

            context.setAuthentication(authenticationToken);
            contextHolder.setContext(context);
        }

        chain.doFilter(request, response);
    }

    private String setResponseHeader(HttpServletRequest request, HttpServletResponse response) {
        return request.getHeader("Authorization");
    }
}
