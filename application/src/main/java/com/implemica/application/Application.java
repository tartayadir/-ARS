package com.implemica.application;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * It is a basic class that contains a method that starts the whole application and points
 * to the location of beans, entities and repositories specifying the base packet. Also,
 * with the help of annotations, changing the settings of the whole application.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.implemica")
@EnableJpaRepositories(basePackages = "com.implemica")
@EntityScan(basePackages = "com.implemica")
@ConfigurationPropertiesScan(basePackages = "com.implemica")
@EnableCaching
@EnableEncryptableProperties
@EnableConfigurationProperties
@EnableSpringConfigured
public class Application {

    /**
     * Standard main method that run whole spring application by static
     * helper that can be used to run a {@link SpringApplication} from the
     * specified source using default settings.
     *
     * @param args standard main input values
     */
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

}
