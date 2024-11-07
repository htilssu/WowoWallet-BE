package com.wowo.wowo.security.filter;

import com.wowo.wowo.models.Partner;
import com.wowo.wowo.repositories.PartnerRepository;
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

@AllArgsConstructor
@Service
public class ApiServiceFilter implements Filter {

    PartnerRepository partnerRepository;

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

        Partner partner = partnerRepository.findPartnerByApiKey(apiKey).orElse(null);
        if (partner == null) {
            chain.doFilter(request, response);
            return;
        }

        final SecurityContextHolderStrategy contextHolderStrategy =
                SecurityContextHolder.getContextHolderStrategy();
        final SecurityContext context = SecurityContextHolder.createEmptyContext();

        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        partner.getId(),
                        apiKey,
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_PARTNER")));
        authenticationToken.setDetails(partner);

        context.setAuthentication(authenticationToken);
        contextHolderStrategy.setContext(context);

        // Debug logging
        System.out.println("ApiServiceFilter set SecurityContext: " + context.getAuthentication());

        chain.doFilter(request, response);
    }
}
