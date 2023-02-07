package com.implemica.application.controller.service.amazonS3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.implemica.controller.exceptions.InvalidImageTypeException;
import com.implemica.controller.service.amazonS3.AmazonClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

@Slf4j
@SpringBootTest
@ActiveProfiles("prod")
class AmazonClientTest {

    private static AmazonClient amazonClient;

    private static AmazonS3 s3client;

    private static String bucketName;

    @BeforeAll
    static void beforeAll() {
        bucketName = AmazonClient.getBucketName();
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

            S3Object s3Object = s3client.getObject(bucketName, fileName);

            assertEquals(fileName, s3Object.getKey());

            s3client.deleteObject(bucketName, fileName);
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
            S3Object s3Object = s3client.getObject(bucketName, fileName);

            assertEquals(fileName, s3Object.getKey());

            amazonClient.deleteFileFromS3Bucket(fileName);

            assertThatThrownBy(() -> s3client.getObject(bucketName, fileName)).
                    isInstanceOf(AmazonS3Exception.class);
        } catch (InvalidImageTypeException e) {

            log.error(e.getMessage(), (Object) e.getStackTrace());
            fail();
        }
    }

    @Autowired
    public void setS3client(AmazonS3 amazonS3) {
        AmazonClientTest.s3client = amazonS3;
    }

    @Autowired
    public void setAmazonClient(AmazonClient amazonClient){
        AmazonClientTest.amazonClient = amazonClient;
    }
}