package com.implemica.controller.service.amazonS3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.implemica.controller.exceptions.InvalidImageTypeException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;

import static com.implemica.controller.utils.ConverterDTO.convertMultiPartToFile;

@Service
@Slf4j
@AllArgsConstructor
@RequiredArgsConstructor
public class AmazonClient {

    @Autowired
    private AmazonS3 s3client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public void uploadFileTos3bucket(String fileName, MultipartFile multipartFile) throws InvalidImageTypeException {

        checkImageType(multipartFile);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setCacheControl("max-age=1209600");

        File file = convertMultiPartToFile(multipartFile);

        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead).withMetadata(objectMetadata));
    }

    public void deleteFileFromS3Bucket(String imageName) {

        s3client.deleteObject(new DeleteObjectRequest(bucketName, imageName));
    }

    private static void checkImageType(MultipartFile image) throws InvalidImageTypeException {

        String imageType = image.getContentType();

        if (!Objects.equals(imageType, "image/png") && !Objects.equals(imageType, "image/jpeg")
                && !Objects.equals(imageType, "image/gif")){

            throw new InvalidImageTypeException("Invalid image type.");
        }
    }
}