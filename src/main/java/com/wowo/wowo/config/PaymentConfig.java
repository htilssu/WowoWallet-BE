package com.wowo.wowo.config;

import com.wowo.wowo.Strategy.AtmCardPaymentStrategy;
import com.wowo.wowo.Strategy.PaypalPaymentStrategy;
import com.wowo.wowo.service.PaypalService;
import com.wowo.wowo.service.TopUpService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    // Đăng ký bean PaypalPaymentStrategy
    @Bean
    public PaypalPaymentStrategy paypalPaymentStrategy(PaypalService paypalService) {
        return new PaypalPaymentStrategy(paypalService);
    }

    // Đăng ký bean AtmCardPaymentStrategy
    @Bean
    public AtmCardPaymentStrategy atmCardPaymentStrategy(TopUpService topUpService) {
        return new AtmCardPaymentStrategy(topUpService);
    }
}