package com.wowo.wowo.strategy;

import com.wowo.wowo.data.dto.TopUpRequestDTO;
import java.util.Map;

public class PaymentStrategyFactory {
    private final Map<TopUpRequestDTO.TopUpSource, PaymentStrategy> strategies;

    public PaymentStrategyFactory(PaypalPaymentStrategy paypalPaymentStrategy,
                                  AtmCardPaymentStrategy atmCardPaymentStrategy) {
        this.strategies = Map.of(
                TopUpRequestDTO.TopUpSource.PAYPAL, paypalPaymentStrategy,
                TopUpRequestDTO.TopUpSource.ATM_CARD, atmCardPaymentStrategy
        );
    }

    public PaymentStrategy getStrategy(TopUpRequestDTO.TopUpSource method) {
        return strategies.getOrDefault(method, (request) -> {
            throw new UnsupportedOperationException("Phương thức thanh toán không được hỗ trợ");
        });
    }
}