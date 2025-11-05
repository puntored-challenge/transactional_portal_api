package com.codechallenge.transactional_portal_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.codechallenge.transactional_portal_api.service.client"})
public class TransactionalPortalApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionalPortalApiApplication.class, args);
    }

}
