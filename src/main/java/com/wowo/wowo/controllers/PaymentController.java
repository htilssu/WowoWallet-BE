package com.wowo.wowo.controllers;

import com.wowo.wowo.annotations.authorized.IsUser;
import com.wowo.wowo.data.dto.OrderDto;
import com.wowo.wowo.kafka.messages.VoucherMessage;
import com.wowo.wowo.kafka.producers.VoucherProducer;
import com.wowo.wowo.models.PaymentStatus;
import com.wowo.wowo.services.OrderService;
import com.wowo.wowo.services.PaymentService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ApiResponse(responseCode = "200", description = "Ok")
@ApiResponse(responseCode = "400", description = "Bad request")
@RequestMapping("v1/pay")
@Tag(name = "Payment", description = "Thanh toán")
@AllArgsConstructor
@IsUser
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderService orderService;
    private final VoucherProducer voucherProducer;

    @PostMapping("/{id}")
    public OrderDto makePay(@PathVariable Long id, Authentication authentication) {

        paymentService.pay(id, authentication);
        final OrderDto orderDetail = orderService.getOrderDetail(id);
        orderDetail.getVouchers()
                .stream()
                .findFirst()
                .ifPresent(_ -> {
                    voucherProducer.sendVoucherMessage(PaymentStatus.SUCCESS.name());
                });

        return orderDetail;
    }
}
