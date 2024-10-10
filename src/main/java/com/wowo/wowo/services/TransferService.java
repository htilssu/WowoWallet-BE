package com.wowo.wowo.services;

import com.wowo.wowo.controllers.TransferControllerV2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    public ResponseEntity<?> transfer(TransferControllerV2.TransferDto data) {


        return ResponseEntity.ok().build();
    }
}
