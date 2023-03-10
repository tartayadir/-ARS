package com.implemica.controller.controllers;

import com.implemica.controller.exceptions.NoSuchCarException;
import com.implemica.controller.service.amazonS3.AmazonClient;
import com.implemica.controller.service.car.service.CarService;
import com.implemica.model.car.dto.CarDTO;
import com.implemica.model.car.entity.Car;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static com.implemica.model.car.dto.CarDTO.toDTO;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/car-catalog")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"Car API"}, description = "Api section for Car service")
public class CarController {

    private final CarService carService;

    private final AmazonClient amazonClient;

    @Operation(summary = "All cars.", description = "Returns all owned ััะบั.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successful.",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = CarDTO.class)))})})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CarDTO>> getAllCars() {

        log.info("Http method - Get, all cars");

        List<CarDTO> dtoList = carService.
                findAll().
                stream().
                map(CarDTO::toDTO).
                collect(Collectors.toList());

        log.info("Number of cars : " + dtoList.size());
        return ResponseEntity.ok().body(dtoList);
    }

    @Operation(summary = "Found car.", description = "Returns the car by id if the car was found. If no such " +
            "machine is found, it returns 404 or if no valid id returns 400.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successful.",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CarDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found.",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid car id.",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE)})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CarDTO> getCar(@Parameter(description = "id of car to return")
                                         @PathVariable Long id) throws NoSuchCarException {

        log.info("Http method - Get, car details with id {}", id);

        Car car = carService.findById(id);

        return ResponseEntity.ok().body(toDTO(car));
    }

    @Operation(summary = "Add car.", description = "Makes the car and returns the car if all fields of the machine are" +
            " valid. If not valid then return 400.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Successful.",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CarDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid car data.",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE)})})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CarDTO> addCar(@Parameter(description = "car that will be added")
                                         @Valid @RequestBody CarDTO carDTO) {

        log.info("Http method - Post, post car");

        URI uri = URI.create(ServletUriComponentsBuilder.
                fromCurrentContextPath().
                path("/car-catalog/add").
                toUriString());

        Car car = carDTO.toEntity();
        car = carService.save(car);

        return ResponseEntity.created(uri).body(toDTO(car));
    }

    @Operation(summary = "Update car.", description = "Finds the car by the id and updates it. If the fields" +
            " are not valid then return 400 or if the cars were not found, return 404")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successful.",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CarDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found and cannot be updated.",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid car.",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE)})})
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CarDTO> updateCar(@Parameter(description = "car that will be updated")
                                            @Valid @RequestBody CarDTO carDTO) throws NoSuchCarException {

        log.info("Http method - Put, update car with id {}", carDTO.getId());

        Car car = carDTO.toEntity();
        carDTO = toDTO(carService.update(car));

        return ResponseEntity.ok().body(carDTO);
    }

    @Operation(summary = "Delete car and their image.",
            description = "Removes machine and image by id. For valid response try integer IDs with positive integer value. " +
                    "Negative or non-integer values will generate API errors")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successful.",
            content = {@Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CarDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found and cannot be deleted.",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid car ID or image file name.",
                    content = {@Content(mediaType = APPLICATION_JSON_VALUE)})
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> removeCar(@Parameter(description = "ID of car that needs to be deleted.")
                                          @PathVariable Long id,
                                          @Parameter(description = "name of mage file that has car needs to be deleted")
                                          @RequestParam String imageId) throws NoSuchCarException {

        log.info("Http method - Delete, delete car with id {}", id);
        carService.deleteById(id);

        log.info("Http method - Delete, delete image with name {}", imageId);
        this.amazonClient.deleteFileFromS3Bucket(imageId);

        return ResponseEntity.ok().build();
    }
}