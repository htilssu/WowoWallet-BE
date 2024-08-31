package com.ewallet.ewallet.controllers;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ApiResponse(responseCode = "200", description = "Ok")
@ApiResponse(responseCode = "400", description = "Bad request")
@RequestMapping("v1/payment")  // Cập nhật version API nếu cần
public class PaymentController {

    @GetMapping("/{id}")
    public ResponseEntity<String> getPayment(@PathVariable String id) {
        String result = "Payment made for id: " + id;
        return ResponseEntity.ok(result);
    }

//    @PostMapping()
//    public ResponseEntity<String> createPayment(@RequestBody PaymentModel paymentModel) {
//        String result = "Payment made for id: " + paymentModel.getId() + " with amount: " + paymentModel.getAmount();
//        return ResponseEntity.ok(result);
//    }
}
