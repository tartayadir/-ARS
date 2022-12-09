package com.implemica.application.controller.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.implemica.controller.controllers.ImageController;
import com.implemica.controller.handlers.ValidationHandler;
import com.implemica.controller.service.amazonS3.AmazonClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.Filter;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.TEXT_PLAIN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.spring.AuthTestUtils.getToken;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class ImageControllerTest {

    private static MockMultipartFile file;

    private static final String fileName = "FileName";

    private static final String expansion = ".jpeg";

    @Autowired
    private AmazonClient amazonClient;

    @Autowired
    private AmazonS3 s3client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

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
        file = new MockMultipartFile(
                "imageFile", fileName + expansion,
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        mockMvc = MockMvcBuilders.
                standaloneSetup(imageController).
                setControllerAdvice(validationHandler).
                addFilters(springSecurityFilterChain).
                build();

        token = getToken();
    }

    @Test
    void uploadFile() throws Exception {

        mockMvc.perform(multipart("/image/upload/{imageName}", fileName).
                file(file).header(AUTHORIZATION, token)).
                andExpect(status().isCreated()).
                andReturn();

        mockMvc.perform(multipart("/image/upload/{imageName}", fileName).
                        file(file)).
                andExpect(status().isForbidden()).
                andReturn();

        S3Object s3Object = s3client.getObject(bucketName, fileName);

        assertEquals(fileName, s3Object.getKey());

        s3client.deleteObject(bucketName, fileName);
    }

    @Test
    void uploadFile_validation() throws Exception{

        file = new MockMultipartFile(
                "imageFile", fileName + ".txt",
                String.valueOf(TEXT_PLAIN),
                "123".getBytes());

        mockMvc.perform(multipart("/image/upload/{imageName}", fileName).
                        file(file).
                        header(AUTHORIZATION, token)).
                andExpect(status().isBadRequest()).
                andDo(result -> System.out.println(result.getResponse().getContentAsString() + " " + result.getResponse().getStatus())).
                andExpect(jsonPath("$.error_message").value("Invalid image type.")).
                andReturn();

        mockMvc.perform(multipart("/image/upload/{imageName}", fileName).
                        file(file)).
                andExpect(status().isForbidden()).
                andReturn();
    }

    @Test
    void deleteFile() throws Exception{

        amazonClient.uploadFileTos3bucket(fileName, file);
        S3Object s3Object = s3client.getObject(bucketName, fileName);

        assertEquals(fileName, s3Object.getKey());

        mockMvc.perform(delete("/image/delete/{imageName}", fileName).
                        header(AUTHORIZATION, token)).
                andExpect(status().isOk()).
                andReturn();

        amazonClient.deleteFileFromS3Bucket(fileName);

        assertThatThrownBy(() -> s3client.getObject(bucketName, fileName)).
                isInstanceOf(AmazonS3Exception.class);
    }
}
