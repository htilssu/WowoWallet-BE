package com.wowo.wowo.controllers;

import com.wowo.wowo.data.dto.request.RefundRequest;
import com.wowo.wowo.data.dto.response.RefundResponse;
import com.wowo.wowo.data.mapper.TransactionMapperImpl;
import com.wowo.wowo.models.Transaction;
import com.wowo.wowo.repositories.OrderRepository;
import com.wowo.wowo.repositories.TransactionRepository;
import com.wowo.wowo.services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "v1/refund", produces = "application/json;charset=UTF-8")
public class RefundController {

    private final TransactionRepository transactionRepository;
    private final TransactionMapperImpl transactionMapperImpl;
    private final TransactionService transactionService;
    private final OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<?> refund(@RequestBody RefundRequest transactionRequest) {
        if (transactionRequest.getTransactionId() == null && transactionRequest.getOrderId() == null) {
            return ResponseEntity.badRequest().build();
        }


        Transaction transaction = null;

        if (transactionRequest.getTransactionId() != null) {
            var transactionOptional = transactionRepository.findById(
                    transactionRequest.getTransactionId());
            if (transactionOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            transaction = transactionOptional.get();
        }

        if (transactionRequest.getOrderId() != null && transaction == null) {
            var paymentRequestOptional = orderRepository.findById(
                    transactionRequest.getOrderId());
            if (paymentRequestOptional.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            transaction = paymentRequestOptional.get().getTransaction();

        }

        switch (transaction.getStatus()) {
            case "REFUNDED":
                return ResponseEntity.ok().body(
                        new RefundResponse(transactionMapperImpl.toResponse(transaction),
                                "Giao dịch đã được hoàn tiền trước đó"));
            case "PENDING":
                return ResponseEntity.ok().body(
                        new RefundResponse(transactionMapperImpl.toResponse(transaction),
                                "Giao dịch đang chờ xử lý"));
            case "FAILED":
                return ResponseEntity.ok().body(
                        new RefundResponse(transactionMapperImpl.toResponse(transaction),
                                "Giao dịch đã thất bại"));
            case "SUCCESS":
                transactionService.refund(transaction);

                break;
            default:
                return ResponseEntity.ok().body(
                        new RefundResponse(transactionMapperImpl.toResponse(transaction),
                                "Giao dịch không hợp lệ"));
        }


        transaction.setStatus("REFUNDED");
        transactionRepository.save(transaction);

        return ResponseEntity.ok(new RefundResponse(transactionMapperImpl.toResponse(transaction),
                "Hoàn tiền thành công"));
    }

}
