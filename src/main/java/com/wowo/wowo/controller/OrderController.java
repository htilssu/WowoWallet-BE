package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsApplication;
import com.wowo.wowo.annotation.authorized.IsAuthenticated;
import com.wowo.wowo.data.dto.OrderCreationDTO;
import com.wowo.wowo.data.dto.OrderDTO;
import com.wowo.wowo.data.mapper.OrderMapperImpl;
import com.wowo.wowo.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
    @IsApplication
    public ResponseEntity<?> createOrder(@RequestBody @NotNull @Validated OrderCreationDTO orderCreationDTO,
            Authentication authentication) {
        return ResponseEntity.status(201)
                .body(
                        orderService.createOrder(orderCreationDTO, authentication));
    }

    @PostMapping("{id}/cancel")
    @IsApplication
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable @NotNull @Validated Long id,
            Authentication authentication) {
        return ResponseEntity.ok(
                orderMapperImpl.toDto(orderService.cancelOrder(id, authentication)));
    }

    @PostMapping("{id}/refund")
    @IsApplication
    public ResponseEntity<OrderDTO> refundOrder(@PathVariable @NotNull @Validated Long id,
            @RequestBody @Validated RefundDTO refundDTO,
            Authentication authentication) {
        return ResponseEntity.ok(
                orderMapperImpl.toDto(orderService.refundOrder(id, refundDTO, authentication)));
    }

    @Data
    public static class RefundDTO {

        @PositiveOrZero(message = "Số tiền hoàn trả phải lớn hơn hoặc bằng 0")
        private Long amount;
    }
}
