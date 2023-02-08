package com.implemica.controller.controllers;

import com.implemica.controller.exceptions.InvalidImageTypeException;
import com.implemica.controller.service.amazonS3.AmazonClient;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"Image API"}, description = "Api section for Image service")
public class ImageController {

    private final AmazonClient amazonClient;

    @Operation(summary = "Upload image.",
            description = "Loads the image to AWS S3 and set image id to this file, if not a graft extension, returns 400 http status.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Image is uploaded successful.",
            content = { @Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Incorrect image file expansion.",
                    content = { @Content(mediaType = APPLICATION_JSON_VALUE)})})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{imageId}", produces = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadFile(@Parameter(description = "image file.")
                                                          @RequestPart("imageFile") MultipartFile image,
                                                      @Parameter(name = "image file name that has car and" +
                                                              " get from AWS S3 by its image file id") @PathVariable String imageId)
            throws InvalidImageTypeException {

        log.info("Http method - Post, post image with name {}", imageId);

        this.amazonClient.uploadFileTos3bucket(imageId, image);

        URI uri = URI.create(ServletUriComponentsBuilder.
                fromCurrentContextPath().
                path(format("/image/upload/%s", imageId)).
                toUriString());

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Delete image.",
            description = "Deletes car image by their name if image is not found returns 404 http status.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Image is deleted successful.",
            content = { @Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", description = "Cannot find image with this name.",
                    content = { @Content(mediaType = APPLICATION_JSON_VALUE)})})
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{imageId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteFile(@PathVariable @Parameter(name = "Image file id")String imageId) {

        log.info("Http method - Delete, delete image with name {}", imageId);
        this.amazonClient.deleteFileFromS3Bucket(imageId);

        return ResponseEntity.ok().build();
    }
}
