package com.ewallet.ewallet.controllers;

import com.ewallet.ewallet.mongo.documents.Equity;
import com.ewallet.ewallet.mongo.documents.EquityDto;
import com.ewallet.ewallet.mongo.documents.EquityMapper;
import com.ewallet.ewallet.mongo.repositories.EquityRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "v1/equity", produces = "application/json; charset=UTF-8")
public class EquityController {

    private final EquityRepository equityRepository;
    private final EquityMapper equityMapper;

    @GetMapping
    public EquityDto getEquity(Authentication authentication) {
        String userId = ((String) authentication.getPrincipal());
        final Optional<Equity> optionalEquity = equityRepository.findByUser(userId);

        return optionalEquity.map(equityMapper::toDto).orElse(null);
    }

}
