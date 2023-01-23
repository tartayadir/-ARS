package com.implemica.application.controller.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.implemica.controller.controllers.ImageController;
import com.implemica.controller.handlers.ValidationHandler;
import com.implemica.controller.service.amazonS3.AmazonClient;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.Filter;

import static com.utils.spring.AuthTestUtils.getAdminToken;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("prod")
public class ImageControllerTest {

    private static MockMultipartFile file;

    private static final String fileName = "FileName";

    private static final String expansion = ".jpeg";

    private static AmazonClient amazonClient;

    private static AmazonS3 s3client;

    private static String bucketName;

    @Autowired
    private ImageController imageController;

    @Autowired
    private ValidationHandler validationHandler;

    @Autowired
    private Filter springSecurityFilterChain;

    private static MockMvc mockMvc;

    private static String token;

    @BeforeEach
    void setUp() {

        bucketName = AmazonClient.getBucketName();

        file = new MockMultipartFile(
                "imageFile", fileName + expansion,
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        mockMvc = MockMvcBuilders.
                standaloneSetup(imageController).
                setControllerAdvice(validationHandler).
                addFilters(springSecurityFilterChain).
                build();

        token = getAdminToken();
    }

    @Test

    void uploadFile() {

        file = new MockMultipartFile(
                "imageFile", fileName + ".jpeg",
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        checkUploadFile(file);

        file = new MockMultipartFile(
                "imageFile", fileName + ".gif",
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        checkUploadFile(file);

        file = new MockMultipartFile(
                "imageFile", fileName + ".png",
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        checkUploadFile(file);
    }

    @Test

    void uploadFile_validation() {

        String invalidFileName = "invalidFile";

        MockMultipartFile invalidFile = new MockMultipartFile(
                "imageFile", invalidFileName + ".txt",
                String.valueOf(TEXT_PLAIN),
                "123".getBytes());
        checkUploadInvalidFile(invalidFile, invalidFileName);

        invalidFile = new MockMultipartFile(
                "imageFile", invalidFileName + ".exe",
                String.valueOf(TEXT_PLAIN),
                "123".getBytes());
        checkUploadInvalidFile(invalidFile, invalidFileName);
    }

    @Test

    void deleteFile() {

        file = new MockMultipartFile(
                "imageFile", fileName + ".jpeg",
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        checkDeleteFile(file);

        file = new MockMultipartFile(
                "imageFile", fileName + ".gif",
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        checkDeleteFile(file);

        file = new MockMultipartFile(
                "imageFile", fileName + ".png",
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        checkDeleteFile(file);
    }

    @SneakyThrows
    private static void checkUploadFile(MockMultipartFile uploadFile) {

        mockMvc.perform(multipart("/image/{imageName}", ImageControllerTest.fileName).
                        file(uploadFile).header(AUTHORIZATION, token)).
                andExpect(status().isCreated()).
                andReturn();

        mockMvc.perform(multipart("/image/{imageName}", ImageControllerTest.fileName).
                        file(uploadFile)).
                andExpect(status().isForbidden()).
                andReturn();

        S3Object s3Object = s3client.getObject(bucketName, ImageControllerTest.fileName);

        assertEquals(ImageControllerTest.fileName, s3Object.getKey());

        s3client.deleteObject(bucketName, ImageControllerTest.fileName);
    }

    @SneakyThrows
    private static void checkUploadInvalidFile(MockMultipartFile uploadFile, String uploadFileName) {

        mockMvc.perform(multipart("/image/{imageName}", uploadFileName).
                        file(uploadFile).
                        header(AUTHORIZATION, token)).
                andExpect(status().isBadRequest()).
                andDo(result -> System.out.println(result.getResponse().getContentAsString() + " " + result.getResponse().getStatus())).
                andExpect(jsonPath("$.error_message").value("Invalid image type.")).
                andReturn();

        mockMvc.perform(multipart("/image/{imageName}", uploadFileName).
                        file(uploadFile)).
                andExpect(status().isForbidden()).
                andReturn();
    }

    @SneakyThrows
    private static void checkDeleteFile(MockMultipartFile deleteFile) {

        amazonClient.uploadFileTos3bucket(ImageControllerTest.fileName, deleteFile);
        S3Object s3Object = s3client.getObject(bucketName, ImageControllerTest.fileName);

        assertEquals(ImageControllerTest.fileName, s3Object.getKey());

        mockMvc.perform(delete("/image/{imageName}", ImageControllerTest.fileName).
                        header(AUTHORIZATION, token)).
                andExpect(status().isOk()).
                andReturn();

        amazonClient.deleteFileFromS3Bucket(ImageControllerTest.fileName);

        assertThatThrownBy(() -> s3client.getObject(bucketName, ImageControllerTest.fileName)).
                isInstanceOf(AmazonS3Exception.class);
    }

    @Autowired
    public void setS3client(AmazonS3 s3client) {
        ImageControllerTest.s3client = s3client;
    }

    @Autowired
    public void setAmazonClient(AmazonClient amazonClient) {
        ImageControllerTest.amazonClient = amazonClient;
    }
}
