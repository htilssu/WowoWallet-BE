package com.wowo.wowo.config;

import com.paypal.sdk.Environment;
import com.paypal.sdk.PaypalServerSDKClient;
import com.paypal.sdk.authentication.ClientCredentialsAuthModel;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaypalConfig {

    @Bean
    public PaypalServerSDKClient paypalServerSDKClient(@Value("${paypal.client.id}") String clientId,
            @Value("${paypal.client.secret}") String clientSecret) {
        return new PaypalServerSDKClient.Builder()
                .loggingConfig(builder -> builder
                        .level(Level.DEBUG)
                        .requestConfig(logConfigBuilder -> logConfigBuilder.body(true))
                        .responseConfig(logConfigBuilder -> logConfigBuilder.headers(true)))
                .httpClientConfig(configBuilder -> configBuilder
                        .timeout(20))
                .clientCredentialsAuth(new ClientCredentialsAuthModel.Builder(
                        clientId,
                        clientSecret)
                        .build())
                .environment(Environment.SANDBOX)
                .build();
    }
}
