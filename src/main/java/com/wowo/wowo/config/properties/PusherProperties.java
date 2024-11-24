package com.wowo.wowo.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("pusher")
@Data
public class PusherProperties {
    private String appId;
    private String key;
    private String secret;
    private String cluster;
}
