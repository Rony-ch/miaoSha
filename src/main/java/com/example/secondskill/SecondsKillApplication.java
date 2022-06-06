package com.example.secondskill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

@SpringBootApplication
public class SecondsKillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondsKillApplication.class, args);
    }
}
