package com.wowo.wowo.controller;

import com.wowo.wowo.data.dto.RefundRequest;
import com.wowo.wowo.data.dto.RefundResponse;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.model.PaymentStatus;
import com.wowo.wowo.model.Transaction;
import com.wowo.wowo.repository.OrderRepository;
import com.wowo.wowo.repository.TransactionRepository;
import com.wowo.wowo.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(value = "v1/refund", produces = "application/json;charset=UTF-8")
@Tag(name = "Refund", description = "Hoàn tiền")
public class RefundController {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapperImpl;
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
            case PaymentStatus.REFUNDED:
                return ResponseEntity.ok().body(
                        new RefundResponse(transactionMapperImpl.toDto(transaction),
                                "Giao dịch đã được hoàn tiền trước đó"));
            case PaymentStatus.PENDING:
                return ResponseEntity.ok().body(
                        new RefundResponse(transactionMapperImpl.toDto(transaction),
                                "Giao dịch đang chờ xử lý"));
            case SUCCESS:
                transactionService.refund(transaction);

                break;
            default:
                return ResponseEntity.ok().body(
                        new RefundResponse(transactionMapperImpl.toDto(transaction),
                                "Giao dịch không hợp lệ"));
        }


        transaction.setStatus(PaymentStatus.REFUNDED);
        transactionRepository.save(transaction);

        return ResponseEntity.ok(new RefundResponse(transactionMapperImpl.toDto(transaction),
                "Hoàn tiền thành công"));
    }

}
