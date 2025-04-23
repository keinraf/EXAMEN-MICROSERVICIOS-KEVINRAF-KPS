package com.kps.kpsprestamoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KpsPrestamoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KpsPrestamoServiceApplication.class, args);
    }

}
