package com.wowo.wowo.security.filter;

import com.wowo.wowo.exceptions.BadRequest;
import com.wowo.wowo.models.Partner;
import com.wowo.wowo.repositories.PartnerRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;

@AllArgsConstructor
@Service
public class ApiServiceFilter implements Filter {

    PartnerRepository partnerRepository;

    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws
                               IOException,
                               ServletException {
        var req = (HttpServletRequest) request;
        var apiKey = req.getHeader("X-API-KEY");


        if (apiKey == null || apiKey.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }


        Partner partner = partnerRepository.findPartnerByApiKey(apiKey).orElseThrow(
                () -> new BadRequest("API key không hợp lệ"));
        //TODO: replace by partner api key

        var context = SecurityContextHolder.getContext();

        final UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(partner.getId()
                        , apiKey,
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_PARTNER")));
        authenticationToken.setDetails(partner);

        context.setAuthentication(authenticationToken);

        req.setAttribute("partner", partner);
        chain.doFilter(request, response);
    }
}
