package com.universite.consumer;

import com.universite.consumer.config.ConfigurationServiceFournisseur;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
public class ApplicationConsommateur {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationConsommateur.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, ConfigurationServiceFournisseur config) {
        return builder
                .setConnectTimeout(Duration.ofMillis(config.getTimeout()))
                .setReadTimeout(Duration.ofMillis(config.getTimeout()))
                .build();
    }
}
