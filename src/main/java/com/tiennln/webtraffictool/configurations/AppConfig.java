package com.tiennln.webtraffictool.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfig {

    @Value("${thread.pool.size}")
    private Integer poolSize;

    @Value("${thread.pool.max.queue.size}")
    private Integer maxQueueSize;

    @Value("${geonode.proxy.type}")
    private String proxyType;
}
