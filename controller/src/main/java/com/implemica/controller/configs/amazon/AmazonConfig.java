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

//        InstanceProfileCredentialsProvider provider = new InstanceProfileCredentialsProvider(true);
//        AmazonS3 s3 = AmazonS3ClientBuilder.standard()
//                .withRegion("us-east-1")
//                .withCredentials(provider)
//                .build();

        //        log.info("region : " + s3.getRegion());
        //        return AmazonS3ClientBuilder.defaultClient();

        return AmazonS3ClientBuilder.defaultClient();
    }

}
