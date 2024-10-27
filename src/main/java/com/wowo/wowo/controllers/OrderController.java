package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsPartner;
import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.OrderCreateDto;
import com.wowo.wowo.data.dto.OrderDto;
import com.wowo.wowo.data.mapper.OrderMapperImpl;
import com.wowo.wowo.services.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "v1/order", produces = "application/json; charset=UTF-8")
@Tag(name = "Order", description = "Đơn hàng")
@IsUser
public class OrderController {

    private final OrderService orderService;
    private final OrderMapperImpl orderMapperImpl;

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        final OrderDto orderDetail = orderService.getOrderDetail(id);
        return ResponseEntity.ok(orderDetail);
    }

    @PostMapping("create")
    @IsPartner
    public ResponseEntity<?> createOrder(@RequestBody @NotNull @Validated OrderCreateDto orderCreateDto, Authentication authentication) {
        return ResponseEntity.status(201).body(orderService.createOrder(orderCreateDto, authentication));
    }

    @PostMapping("{id}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable @NotNull @Validated Long id,
            Authentication authentication) {
        return ResponseEntity.ok(
                orderMapperImpl.toDto(orderService.cancelOrder(id, authentication)));
    }

    @PostMapping("{id}/refund")
    public ResponseEntity<OrderDto> refundOrder(@PathVariable @NotNull @Validated Long id,
            Authentication authentication) {
        return ResponseEntity.ok(
                orderMapperImpl.toDto(orderService.refundOrder(id, authentication)));
    }
}
