package com.ewallet.ewallet.controllers;

import com.ewallet.ewallet.dto.mapper.PaymentRequestMapperImpl;
import com.ewallet.ewallet.dto.mapper.TransactionMapperImpl;
import com.ewallet.ewallet.models.Partner;
import com.ewallet.ewallet.models.PaymentRequest;
import com.ewallet.ewallet.models.Transaction;
import com.ewallet.ewallet.payment.PaymentService;
import com.ewallet.ewallet.payment.request.PaymentRequestData;
import com.ewallet.ewallet.repositories.PaymentRequestRepository;
import com.ewallet.ewallet.util.ObjectUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v?/prequest", produces = "application/json; charset=UTF-8")
public class PaymentRequestController {

    private final PaymentRequestMapperImpl paymentRequestMapperImpl;
    private final HttpServletRequest httpServletRequest;
    private final PaymentService paymentService;
    private final TransactionMapperImpl transactionMapperImpl;
    PaymentRequestRepository paymentRequestRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id, Authentication authentication) {
        Optional<PaymentRequest> orderOptional = paymentRequestRepository.findById(id);
        if (orderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PaymentRequest order = orderOptional.get();
        return ResponseEntity.ok(paymentRequestMapperImpl.toDto(order));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> makePay(Authentication authentication, @PathVariable String id) {
        if (id == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Optional<PaymentRequest> paymentRequestOptional = paymentRequestRepository.findById(
                id);

        if (paymentRequestOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PaymentRequest paymentRequest = paymentRequestOptional.get();
        if (!paymentRequest.getStatus().equals("PENDING")) {
            return ResponseEntity.badRequest().body(null);
        }

        final Partner partner = paymentRequest.getPartner();
        if (partner == null) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            final Transaction transaction = paymentService.makePayment(paymentRequest,
                    authentication);

            return ResponseEntity.ok(transactionMapperImpl.toResponse(transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping
    @PreAuthorize("hasRole('PARTNER')")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequestData paymentRequest,
            HttpServletRequest request) {
        if (paymentRequest == null || paymentRequest.getMoney() < 0 || paymentRequest.getVoucherId() == null || paymentRequest.getVoucherName() == null) {
            return ResponseEntity.badRequest().body("Tiền không đươc bé hơn 0, voucherId not null, voucherName not null");
        }

        PaymentRequest newPaymentRequest = paymentRequestMapperImpl.toEntity(paymentRequest);
        newPaymentRequest.setPartner(((Partner) httpServletRequest.getAttribute("partner")));

        PaymentRequest savedEntity = paymentRequestRepository.save(newPaymentRequest);
        return ResponseEntity.ok(
                ObjectUtil.mergeObjects(paymentRequestMapperImpl.toDto(savedEntity),
                        ObjectUtil.wrapObject("checkUrl",
                                request.getRequestURL().toString() + "/" + savedEntity.getId())));
    }
}
