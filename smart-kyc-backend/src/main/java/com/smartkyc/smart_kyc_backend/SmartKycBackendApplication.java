package com.smartkyc.smart_kyc_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.smartkyc.entity")
@ComponentScan({
    "com.smartkyc.controller",
    "com.smartkyc.service",
    "com.smartkyc.repository"
})
@EnableJpaRepositories("com.smartkyc.repository")
public class SmartKycBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(
            SmartKycBackendApplication.class,
            args
        );
    }
}