package com.implemica.controller.service.amazonS3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.implemica.controller.configs.amazon.AmazonConfig;
import com.implemica.controller.exceptions.InvalidImageTypeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;

import static com.implemica.controller.utils.FileConvertor.convertMultiPartToFile;
import static java.lang.String.format;

/**
 * Provides methods for work with images and AWS S3 bucket. In particular, validate images,
 * upload and delete cars images in bucket. Bucket setting can be set up by
 * application.properties file.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AmazonClient {

    /**
     * Provided {@link AmazonS3} from {@link AmazonConfig}
     */
    private final AmazonS3 s3client;

    /**
     * S3 bucket name where car images will be uploaded
     */
    private static String bucketName;

    /**
     * Cache control header name
     */
    private static final String CACHE_CONTROL_HEADER_PREFIX = "max-age";

    /**
     * Cache control value for uploading files
     */
    @Value("${cars.s3.images_cahcecontrol}")
    private int cacheControlDuration;

    public static final String DEFAULT_IMAGE_ID = "default-car-image";

    /**
     * Check and upload file to S3 bucket.
     *
     * @param imageID image ID
     * @param imageFile image file
     * @throws InvalidImageTypeException if image have invalid expansion
     */
    public void uploadFileTos3bucket(String imageID, MultipartFile imageFile) throws InvalidImageTypeException {

        log.info("bucket name : " + bucketName);

        checkImageType(imageFile);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setCacheControl(format("%s=%d", CACHE_CONTROL_HEADER_PREFIX, cacheControlDuration));

        File file = convertMultiPartToFile(imageFile);

        s3client.putObject(new PutObjectRequest(bucketName, imageID, file)
                .withCannedAcl(CannedAccessControlList.PublicRead).withMetadata(objectMetadata));

        file.delete();
    }

    /**
     * Deletes image file on bucket by image ID
     *
     * @param imageID image ID that use for found and delete image
     */
    public void deleteFileFromS3Bucket(String imageID) {

        if(!imageID.equals(DEFAULT_IMAGE_ID)){

            s3client.deleteObject(new DeleteObjectRequest(bucketName, imageID));
        }

    }

    /**
     * Verifies the file is an image
     *
     * @param image file for check
     * @throws InvalidImageTypeException if image have invalid expansion
     */
    private static void checkImageType(MultipartFile image) throws InvalidImageTypeException {

        String imageType = image.getContentType();

        if (!Objects.equals(imageType, "image/png") && !Objects.equals(imageType, "image/jpeg")
                && !Objects.equals(imageType, "image/gif")){

            throw new InvalidImageTypeException("Invalid image type.");
        }
    }

    /**
     * Set bucket name for uploading images from application properties file
     *
     * @param bucketName provided bucket name
     */
    @Value("${cars.s3.bucket_name}")
    public void setBucketName(String bucketName) {
        AmazonClient.bucketName = bucketName;
    }

    /**
     * Getter for bucket name
     *
     * @return bucket name
     */
    public static String getBucketName() {
        return bucketName;
    }
}