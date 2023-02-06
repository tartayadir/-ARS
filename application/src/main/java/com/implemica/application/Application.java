package com.implemica.application;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.implemica")
@EnableJpaRepositories(basePackages = "com.implemica")
@EntityScan(basePackages = "com.implemica")
@EnableCaching
@EnableEncryptableProperties
@Slf4j
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

}
