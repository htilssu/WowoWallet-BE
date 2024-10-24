package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.OrderCreateDto;
import com.wowo.wowo.data.dto.OrderDto;
import com.wowo.wowo.data.mapper.OrderMapper;
import com.wowo.wowo.data.mapper.TransactionMapper;
import com.wowo.wowo.models.Order;
import com.wowo.wowo.repositories.OrderRepository;
import com.wowo.wowo.services.OrderService;
import com.wowo.wowo.services.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping(value = "v1/order", produces = "application/json; charset=UTF-8")
@Tag(name = "Order", description = "Đơn hàng")
@IsUser
public class OrderController {

    private final OrderMapper orderMapper;
    private final HttpServletRequest httpServletRequest;
    private final PaymentService paymentService;
    private final TransactionMapper transactionMapperImpl;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id, Authentication authentication) {
        final OrderDto orderDetail = orderService.getOrderDetail(id);
        return ResponseEntity.ok(orderDetail);
    }

    @PostMapping("create")
    public ResponseEntity<?> createOrder(@RequestBody @NotNull OrderCreateDto orderCreateDto) {
        return ResponseEntity.ok(orderService.createOrder(orderCreateDto));
    }
}
