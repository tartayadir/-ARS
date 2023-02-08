package com.implemica.controller.configs.amazon;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class creating client {@link AmazonS3} to work with AWS S3 bucket where
 * to store pictures of cars. To access the bucket used instance AWS credentials.
 */
@Configuration
public class AmazonConfig {

    /**
     * Method provides a bean for accessing the Amazon S3 web service.
     * Use the specified region and instance AWS credentials.
     *
     * @return {@link AmazonS3} object to access to AWS S3 web service.
     */
    @Bean
    public AmazonS3 s3() {

        AWSCredentialsProvider awsCredentialsProvider = new DefaultAWSCredentialsProviderChain();
        String bucketRegion = "us-east-1";

        return AmazonS3ClientBuilder.standard()
                .withCredentials(awsCredentialsProvider)
                .withRegion(bucketRegion)
                .build();
    }

}
