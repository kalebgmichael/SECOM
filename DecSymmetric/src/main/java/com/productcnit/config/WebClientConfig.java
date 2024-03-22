package com.productcnit.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Bean
    @LoadBalanced     //create automatically load balancing when making a client request
    public WebClient.Builder webClientBuilder()
    {
        return WebClient.builder(); // create a bean of the method name webClient of type webclient
    }
}
