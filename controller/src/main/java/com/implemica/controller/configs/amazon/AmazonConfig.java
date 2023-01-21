package com.implemica.controller.configs.amazon;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
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

//        AWSCredentialsProviderChain providerChain = new AWSCredentialsProviderChain(
//                InstanceProfileCredentialsProvider.getInstance(),
//                new ProfileCredentialsProvider()
//        );
//
//        return AmazonS3ClientBuilder.standard()
//                .withCredentials(providerChain)
//                .build();
//
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withRegion("us-east-1")
                .build();
//        return AmazonS3ClientBuilder.defaultClient();
    }

}
