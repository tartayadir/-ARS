package com.implemica.application.controller.service.amazonS3;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.implemica.controller.exceptions.InvalidImageTypeException;
import com.implemica.controller.service.amazonS3.AmazonClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.http.MediaType.*;
import static org.springframework.http.MediaType.IMAGE_JPEG;

@Slf4j
@SpringBootTest(classes = SpringBootTest.class)
@ExtendWith(MockitoExtension.class)
class AmazonClientTest {

    @Value("${aws.access.key.id}")
    private String accessKey;

    @Value("${aws.secret.access.key}")
    private String secretKey;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private static AmazonClient amazonClient;

    private static final String expansion = ".jpeg";

    private static AmazonS3 s3client;

    @BeforeEach
    void setUp() {

        AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

        s3client = AmazonS3ClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();

        amazonClient = new AmazonClient(s3client, bucketName);


    }

    @Test
    void uploadFileTos3bucket() {

        String fileName = "testFile";
        MultipartFile file = new MockMultipartFile(
                "imageFile", fileName + ".jpeg",
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        uploadAndCheckFile(file, fileName);

        file = new MockMultipartFile(
                "imageFile", fileName + ".gif",
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        uploadAndCheckFile(file, fileName);

        file = new MockMultipartFile(
                "imageFile", fileName + ".png",
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        uploadAndCheckFile(file, fileName);
    }

    @Test
    void uploadFileTos3bucket_invalid_image() {

        String fileName = "testFile";
        MultipartFile multipartFile = new MockMultipartFile(
                "imageFile", fileName + ".txt",
                TEXT_HTML_VALUE,
                "123".getBytes());

        uploadAndCheckInvalidFile(multipartFile, fileName);

        multipartFile = new MockMultipartFile(
                "imageFile", fileName + ".exe",
                TEXT_HTML_VALUE,
                "123".getBytes());

        uploadAndCheckInvalidFile(multipartFile, fileName);
    }

    @Test
    void deleteFileFromS3Bucket() {

        String fileName = "testFile";
        MultipartFile file = new MockMultipartFile(
                "imageFile", fileName + ".jpeg",
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        checkDeleteFile(file, fileName);

        file = new MockMultipartFile(
                "imageFile", fileName + ".gif",
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        checkDeleteFile(file, fileName);

        file = new MockMultipartFile(
                "imageFile", fileName + ".png",
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        checkDeleteFile(file, fileName);
    }

    private static void uploadAndCheckFile(MultipartFile multipartFile, String fileName) {

        try {
            amazonClient.uploadFileTos3bucket(fileName, multipartFile);

            S3Object s3Object = s3client.getObject("carsbucketspringboot", fileName);

            assertEquals(fileName, s3Object.getKey());

            s3client.deleteObject("carsbucketspringboot", fileName);
        } catch (InvalidImageTypeException e) {

            log.error(e.getMessage(), (Object) e.getStackTrace());
            fail();
        }
    }

    private static void uploadAndCheckInvalidFile(MultipartFile multipartFile, String fileName) {

        assertThatThrownBy(() -> amazonClient.uploadFileTos3bucket(fileName, multipartFile)).
                isInstanceOf(InvalidImageTypeException.class).
                hasMessageContaining("Invalid image type.");
    }

    private static void checkDeleteFile(MultipartFile multipartFile, String fileName) {

        try {

            amazonClient.uploadFileTos3bucket(fileName, multipartFile);
            S3Object s3Object = s3client.getObject("carsbucketspringboot", fileName);

            assertEquals(fileName, s3Object.getKey());

            amazonClient.deleteFileFromS3Bucket(fileName);

            assertThatThrownBy(() -> s3client.getObject("carsbucketspringboot", fileName)).
                    isInstanceOf(AmazonS3Exception.class);
        } catch (InvalidImageTypeException e) {

            log.error(e.getMessage(), (Object) e.getStackTrace());
            fail();
        }
    }
}