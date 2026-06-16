package com.universite.consumer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationServiceFournisseur {

    @Value("${provider.service.base-url}")
    private String baseUrl;

    @Value("${provider.service.timeout}")
    private int timeout;

    public String getBaseUrl() {
        return baseUrl;
    }

    public int getTimeout() {
        return timeout;
    }
}
