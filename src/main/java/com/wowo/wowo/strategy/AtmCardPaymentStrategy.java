package com.wowo.wowo.strategy;

import com.wowo.wowo.data.dto.TopUpRequestDTO;
import com.wowo.wowo.service.TopUpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
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
        log.info("Top up request processed.");
    }

}
