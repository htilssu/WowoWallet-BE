package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsAuthenticated;
import com.wowo.wowo.annotation.authorized.IsPartner;
import com.wowo.wowo.data.dto.OrderCreateDTO;
import com.wowo.wowo.data.dto.OrderDTO;
import com.wowo.wowo.data.mapper.OrderMapperImpl;
import com.wowo.wowo.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "v1/orders", produces = "application/json; charset=UTF-8")
@Tag(name = "Order", description = "Đơn hàng")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapperImpl orderMapperImpl;

    @IsAuthenticated
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        final OrderDTO orderDetail = orderService.getOrderDetail(id);
        return ResponseEntity.ok(orderDetail);
    }

    @PostMapping("create")
    @IsPartner
    public ResponseEntity<?> createOrder(@RequestBody @NotNull @Validated OrderCreateDTO orderCreateDTO,
            Authentication authentication) {
        return ResponseEntity.status(201).body(
                orderService.createOrder(orderCreateDTO, authentication));
    }

    @PostMapping("{id}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable @NotNull @Validated Long id,
            Authentication authentication) {
        return ResponseEntity.ok(
                orderMapperImpl.toDTO(orderService.cancelOrder(id, authentication)));
    }

    @PostMapping("{id}/refund")
    public ResponseEntity<OrderDTO> refundOrder(@PathVariable @NotNull @Validated Long id,
            Authentication authentication) {
        return ResponseEntity.ok(
                orderMapperImpl.toDTO(orderService.refundOrder(id, authentication)));
    }
}
