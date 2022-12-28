package com.implemica.controller.configs.amazon;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {

//    @Value("${aws.access.key.id}")
//    private String accessKey;
//    @Value("${aws.secret.access.key}")
//    private String secretKey;
//    @Value(" ${aws.s3.region}")
//    private String region;

    @Bean
    public AmazonS3 s3() {

//        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.defaultClient();
//        amazonS3.setRegion(Region.getRegion(Regions.EU_CENTRAL_1));

        return amazonS3;
    }
}
