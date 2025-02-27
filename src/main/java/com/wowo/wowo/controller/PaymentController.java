package com.wowo.wowo.controller;

import com.wowo.wowo.annotation.authorized.IsUser;
import com.wowo.wowo.data.dto.OrderDTO;
import com.wowo.wowo.kafka.message.VoucherMessage;
import com.wowo.wowo.kafka.producer.VoucherProducer;
import com.wowo.wowo.model.Order;
import com.wowo.wowo.model.PaymentStatus;
import com.wowo.wowo.service.OrderService;
import com.wowo.wowo.service.PaymentService;
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
    public OrderDTO makePay(@PathVariable Long id, Authentication authentication) {

        final Order order = paymentService.pay(id, authentication);
        voucherProducer.sendVoucherMessage(
                new VoucherMessage(order.getId(), PaymentStatus.SUCCESS));
        return orderService.getOrderDetail(id);
    }
}
