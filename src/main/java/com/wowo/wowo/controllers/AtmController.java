package com.wowo.wowo.controllers;

import com.wowo.wowo.models.Atm;
import com.wowo.wowo.repositories.AtmRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "v1/atm", produces = "application/json; charset=utf-8")
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
