package com.productcnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class KeyExchangeProtocolApplication {
    public static void main(String[] args) {
        SpringApplication.run(KeyExchangeProtocolApplication.class,args);
    }

}
