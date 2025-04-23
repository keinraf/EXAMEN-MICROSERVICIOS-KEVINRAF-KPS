package com.kps.kpsconfigservidor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class KpsConfigServidorApplication {

    public static void main(String[] args) {
        SpringApplication.run(KpsConfigServidorApplication.class, args);
    }

}



