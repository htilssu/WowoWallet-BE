package com.wowo.wowo.Strategy;

import com.wowo.wowo.data.dto.TopUpRequestDTO;
import com.wowo.wowo.service.TopUpService;
import org.springframework.stereotype.Service;

@Service
public class AtmCardPaymentStrategy implements PaymentStrategy {
    private final TopUpService topUpService;

    public AtmCardPaymentStrategy(TopUpService topUpService) {
        this.topUpService = topUpService;
    }

    @Override
    public void processPayment(TopUpRequestDTO request) {
        // TODO: Kiểm tra thẻ hợp lệ trong database trước khi xử lý
        topUpService.topUpWithLimit(request.getTo(), request.getAmount());
        System.out.println("Đã xử lý thanh toán bằng ATM Card");
    }

}
