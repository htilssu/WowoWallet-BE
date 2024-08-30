package com.ewallet.ewallet.link_service;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Service {
    @Id
    String id;
    String name;
    String apiKey;
    String serviceType;
}
