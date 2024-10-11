package com.wowo.wowo.controllers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v2/transfer")
public class TransferControllerV2 {

    @PostMapping
    public ResponseEntity<?> transfer(@Validated @RequestBody TransferDto data) {


        return ResponseEntity.ok().build();
    }

    @Getter
    @Setter
    public static class TransferDto {

        private String senderId;
        private String receiverId;
        private Long money;
        private String description;
    }
}
