package com.wowo.wowo.controller;

import com.wowo.wowo.model.Atm;
import com.wowo.wowo.repository.AtmRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "v1/atm", produces = "application/json; charset=utf-8")
@Tag(name = "ATM", description = "ATM")
public class AtmController {

    private final AtmRepository atmRepository;

    public AtmController(AtmRepository atmRepository) {
        this.atmRepository = atmRepository;
    }

    @GetMapping
    public List<Atm> getAtms() {
        return atmRepository.findAll();
    }
}
