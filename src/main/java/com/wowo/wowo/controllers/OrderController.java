package com.wowo.wowo.controllers;

import com.wowo.wowo.data.dto.CreateOrderData;
import com.wowo.wowo.data.mapper.OrderMapper;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.models.Order;
import com.wowo.wowo.models.Partner;
import com.wowo.wowo.repositories.OrderRepository;
import com.wowo.wowo.services.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(value = "v1/order", produces = "application/json; charset=UTF-8")
@Tag(name = "Order", description = "Đơn hàng")
public class OrderController {

    private final OrderMapper orderMapper;
    private final HttpServletRequest httpServletRequest;
    private final PaymentService paymentService;
    private final TransactionMapper transactionMapperImpl;
    private final OrderRepository orderRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id, Authentication authentication) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Order order = orderOptional.get();
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    /*@PostMapping("/{id}")
    public ResponseEntity<?> createOrder(Authentication authentication, @PathVariable String id) {
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

    }*/

    @PostMapping
    @PreAuthorize("hasRole('PARTNER')")
    public ResponseEntity<?> createOrder(@RequestBody @NotNull CreateOrderData paymentRequest,
            HttpServletRequest request) {

        Order newOrder = orderMapper.toEntity(paymentRequest);
        newOrder.setPartner(((Partner) httpServletRequest.getAttribute("partner")));

        Order savedEntity = orderRepository.save(newOrder);
        return ResponseEntity.ok(orderMapper.toDto(savedEntity));
    }
}
