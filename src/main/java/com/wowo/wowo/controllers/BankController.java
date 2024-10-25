package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.AllowAnonymous;
import com.wowo.wowo.models.Bank;
import com.wowo.wowo.repositories.BankRepostitory;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping(value = "v1/banks", produces = "application/json; charset=utf-8")
public class BankController {

    private final BankRepostitory bankRepostitory;

    public BankController(BankRepostitory bankRepostitory) {
        this.bankRepostitory = bankRepostitory;
    }

    @GetMapping
    @AllowAnonymous
    public ResponseEntity<List<Bank>> getBanks() {
        List<Bank> banks = bankRepostitory.findAll(); // Lấy danh sách ngân hàng
        if (banks.isEmpty()) {
            return ResponseEntity.noContent()
                    .build(); // Trả về 204 No Content nếu không có ngân hàng nào
        }
        return ResponseEntity.status(200).cacheControl(
                CacheControl.maxAge(Duration.ofHours(1))).body(banks); // Trả về danh sách ngân hàng
    }
}
