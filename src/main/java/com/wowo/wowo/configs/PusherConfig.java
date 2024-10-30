package com.wowo.wowo.configs;

import com.pusher.rest.Pusher;
import com.pusher.rest.PusherAsync;
import com.wowo.wowo.configs.properties.PusherProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PusherProperties.class)
public class PusherConfig {

    @Bean
    public Pusher pusher(PusherProperties properties) {
        final Pusher pusher = new Pusher(
                properties.getAppId(), properties.getKey(), properties.getSecret());
        pusher.setCluster(properties.getCluster());
        return pusher;
    }

    @Bean
    public PusherAsync asyncPusher(PusherProperties properties) {
        final PusherAsync pusherAsync = new PusherAsync(
                properties.getAppId(), properties.getKey(), properties.getSecret());
        pusherAsync.setCluster(properties.getCluster());
        return pusherAsync;
    }
}
