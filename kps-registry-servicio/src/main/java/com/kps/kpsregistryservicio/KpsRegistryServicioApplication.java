package com.kps.kpsregistryservicio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class KpsRegistryServicioApplication {

    public static void main(String[] args) {
        SpringApplication.run(KpsRegistryServicioApplication.class, args);
    }

}
