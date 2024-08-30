package com.ewallet.ewallet.controllers;

import com.ewallet.ewallet.dto.mapper.TransactionMapperImpl;
import com.ewallet.ewallet.data.dto.request.RefundRequest;
import com.ewallet.ewallet.data.dto.response.RefundResponse;
import com.ewallet.ewallet.models.Transaction;
import com.ewallet.ewallet.repositories.PaymentRequestRepository;
import com.ewallet.ewallet.repositories.TransactionRepository;
import com.ewallet.ewallet.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v?/refund", produces = "application/json;charset=UTF-8")
public class RefundController {

    private final TransactionRepository transactionRepository;
    private final TransactionMapperImpl transactionMapperImpl;
    private final PaymentRequestRepository paymentRequestRepository;
    private final TransactionService transactionService;

    public RefundController(TransactionRepository transactionRepository,
            TransactionMapperImpl transactionMapperImpl,
            PaymentRequestRepository paymentRequestRepository,
            TransactionService transactionService) {
        this.transactionRepository = transactionRepository;
        this.transactionMapperImpl = transactionMapperImpl;
        this.paymentRequestRepository = paymentRequestRepository;
        this.transactionService = transactionService;
    }

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
            var paymentRequestOptional = paymentRequestRepository.findByOrderId(
                    transactionRequest.getOrderId());
            if (paymentRequestOptional == null) {
                return ResponseEntity.notFound().build();
            }

            transaction = paymentRequestOptional.getTransaction();

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
