package com.implemica.controller.configs.amazon;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class AmazonConfig {

    @Bean
    public AmazonS3 s3() {

        return AmazonS3ClientBuilder.defaultClient();
    }

}
