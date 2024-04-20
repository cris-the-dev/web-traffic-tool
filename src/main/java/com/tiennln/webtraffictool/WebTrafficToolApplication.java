package com.tiennln.webtraffictool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WebTrafficToolApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebTrafficToolApplication.class, args);
    }

}
