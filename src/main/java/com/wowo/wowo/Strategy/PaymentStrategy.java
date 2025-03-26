package com.wowo.wowo.Strategy;

import com.wowo.wowo.data.dto.TopUpRequestDTO;

public interface PaymentStrategy {
    void processPayment(TopUpRequestDTO request);
}
