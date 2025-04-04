package com.wowo.wowo.strategy;

import com.wowo.wowo.data.dto.TopUpRequestDTO;

public interface PaymentStrategy {
    void processPayment(TopUpRequestDTO request);
}
