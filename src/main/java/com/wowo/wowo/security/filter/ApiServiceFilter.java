package com.wowo.wowo.security.filter;

import com.wowo.wowo.model.Application;
import com.wowo.wowo.service.ApplicationServiceImpl;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ApiServiceFilter implements Filter {

    ApplicationServiceImpl applicationService;

    // ApiServiceFilter.java
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
                                                                                              IOException,
                                                                                              ServletException {
        var req = (HttpServletRequest) request;
        var apiKey = req.getHeader("X-API-KEY");

        if (apiKey == null || apiKey.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        Optional<Application> application = applicationService.getApplicationBySecretKey(apiKey);
        if (application.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        final SecurityContextHolderStrategy contextHolderStrategy =
                SecurityContextHolder.getContextHolderStrategy();
        final SecurityContext context = SecurityContextHolder.createEmptyContext();

        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        application.get()
                                .getId(),
                        apiKey,
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_APPLICATION")));
        authenticationToken.setDetails(application);

        context.setAuthentication(authenticationToken);
        contextHolderStrategy.setContext(context);

        chain.doFilter(request, response);
    }
}
