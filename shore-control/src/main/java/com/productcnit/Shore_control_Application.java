package com.productcnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Shore_control_Application {
    public static void main(String[] args) {
        SpringApplication.run(Shore_control_Application.class,args);
    }
}
