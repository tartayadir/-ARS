package com.implemica.controller.service.amazonS3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.implemica.controller.exceptions.InvalidImageTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;

import static com.implemica.controller.utils.ConverterDTO.convertMultiPartToFile;
import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class AmazonClient {

    private final AmazonS3 s3client;

    private static String bucketName;

    private static final String CACHE_CONTROL_HEADER_PREFIX = "max-age";

    @Value("${cars.s3.images_cahcecontrol}")
    private int cacheControlDuration;

    public void uploadFileTos3bucket(String fileName, MultipartFile multipartFile) throws InvalidImageTypeException {

        log.info("bucket name : " + bucketName);

        checkImageType(multipartFile);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setCacheControl(format("%s=%d", CACHE_CONTROL_HEADER_PREFIX, cacheControlDuration));

        File file = convertMultiPartToFile(multipartFile);

        s3client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead).withMetadata(objectMetadata));

        file.delete();
    }

    public void deleteFileFromS3Bucket(String imageName) {

        if(!imageName.equals("default-car-image")){

            s3client.deleteObject(new DeleteObjectRequest(bucketName, imageName));
        }

    }

    private static void checkImageType(MultipartFile image) throws InvalidImageTypeException {

        String imageType = image.getContentType();

        if (!Objects.equals(imageType, "image/png") && !Objects.equals(imageType, "image/jpeg")
                && !Objects.equals(imageType, "image/gif")){

            throw new InvalidImageTypeException("Invalid image type.");
        }
    }

    @Value("${cars.s3.bucket_name}")
    public void setBucketName(String bucketName) {
        AmazonClient.bucketName = bucketName;
    }

    public static String getBucketName() {
        return bucketName;
    }
}