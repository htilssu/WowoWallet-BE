package com.wowo.wowo.services;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

@ConfigurationProperties(prefix = "paypal")
@Service
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaypalService {

    String url;
    HttpURLConnection clientConnection;

    @PostConstruct
    public void init() {
        try {
            URI uri = new URI(url);
            clientConnection = (HttpURLConnection) uri.toURL().openConnection();

        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
