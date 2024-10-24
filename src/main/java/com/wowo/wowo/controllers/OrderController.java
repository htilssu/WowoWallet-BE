package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.OrderCreateDto;
import com.wowo.wowo.data.dto.OrderDto;
import com.wowo.wowo.services.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "v1/order", produces = "application/json; charset=UTF-8")
@Tag(name = "Order", description = "Đơn hàng")
@IsUser
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        final OrderDto orderDetail = orderService.getOrderDetail(id);
        return ResponseEntity.ok(orderDetail);
    }

    @PostMapping("create")
    public ResponseEntity<?> createOrder(@RequestBody @NotNull OrderCreateDto orderCreateDto) {
        return ResponseEntity.status(201).body(orderService.createOrder(orderCreateDto));
    }
}
