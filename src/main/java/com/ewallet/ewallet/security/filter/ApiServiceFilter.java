package com.ewallet.ewallet.security.filter;

import com.ewallet.ewallet.models.Partner;
import com.ewallet.ewallet.repositories.PartnerRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ApiServiceFilter implements Filter {

    PartnerRepository partnerRepository;

    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws
                               IOException,
                               ServletException {
        var req = (HttpServletRequest) request;
        var apiKey = req.getHeader("X-Api");


        if (apiKey == null || apiKey.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }


        Optional<Partner> partner = Optional.empty();
        try {
            partner = partnerRepository.findPartnerByApiKey(apiKey);
            partner.ifPresent(value -> value.setPassword(null));
        } catch (RuntimeException r) {
            System.out.println("Error: " + r.getMessage());
        }

        if (partner.isPresent()) {
            var context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();

            if (authentication == null) {
                context.setAuthentication(new UsernamePasswordAuthenticationToken(partner.get()
                        , apiKey,
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_PARTNER"))));
            }

            req.setAttribute("partner", partner.get());
            chain.doFilter(request, response);
            return;
        }

        HttpServletResponse res = (HttpServletResponse) response;
        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        res.getWriter().println("{\"message\": \"API key không hợp lệ\"}");
        res.setContentType("application/json");
    }
}
