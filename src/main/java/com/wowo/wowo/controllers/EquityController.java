package com.wowo.wowo.controllers;

import com.wowo.wowo.mongo.documents.Equity;
import com.wowo.wowo.mongo.documents.EquityDto;
import com.wowo.wowo.mongo.documents.EquityMapper;
import com.wowo.wowo.mongo.repositories.EquityRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping(value = "v1/equity", produces = "application/json; charset=UTF-8")
@Tag(name = "Equity", description = "Biến động số dư")
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
