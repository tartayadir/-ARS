package com.implemica.controller.controllers;

import com.implemica.controller.exceptions.NoSuchCarException;
import com.implemica.controller.service.amazonS3.AmazonClient;
import com.implemica.controller.service.car.service.CarService;
import com.implemica.controller.utils.ConverterDTO;
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

import static com.implemica.controller.utils.ConverterDTO.carEntityToDTO;
import static com.implemica.controller.utils.ConverterDTO.dtoToCarEntity;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/car-catalog")
@RequiredArgsConstructor
@Slf4j
@Api(tags = {"Car API"}, description = "Api section for Car service")
public class CarController {

    private final CarService carService;

    private final AmazonClient amazonClient;

    @Operation(summary = "All cars list.", description = "Brings up a list of all the cars.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successful found all cars.",
            content = { @Content(mediaType = APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = CarDTO.class)))})})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/all-cars", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CarDTO>> getAllCars() {

        log.info("Http method - Get, all cars");

        List<CarDTO> dtoList = carService.
                findAll().
                stream().
                map(ConverterDTO::carEntityToDTO).
                collect(Collectors.toList());

        log.info("Number of cars : " + dtoList.size());
        return ResponseEntity.ok().body(dtoList);
    }

    @Operation(summary = "Found car.", description = "Returns found car by its id.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Successful found the car",
            content = { @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CarDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found.",
                    content = { @Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid car ID.",
                    content = { @Content(mediaType = APPLICATION_JSON_VALUE)})
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/details/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CarDTO> getCar(@Parameter(description = "ID of car to return")
                                             @PathVariable Long id) throws NoSuchCarException {

        log.info("Http method - Get, car details with id {}", id);

        Car car = carService.findById(id);
        CarDTO carDTO = carEntityToDTO(car);

        return ResponseEntity.ok().body(carDTO);
    }

    @Operation(summary = "Add car.", description = "Adds a new car and returns it.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Car is added successful.",
            content = { @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CarDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid car data.",
              content = { @Content(mediaType = APPLICATION_JSON_VALUE)})})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/add", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CarDTO> addCar(@Parameter(description = "car that will be added")
                                             @Valid @RequestBody CarDTO carDTO) {

        log.info("Http method - Post, post car");

        URI uri = URI.create(ServletUriComponentsBuilder.
                fromCurrentContextPath().
                path("/car-catalog/add").
                toUriString());

        Car car = dtoToCarEntity(carDTO);
        car = carService.save(car);
        carDTO = carEntityToDTO(car);

        return ResponseEntity.created(uri).body(carDTO);
    }

    @Operation(summary = "Update car.", description = "Updates a car and returns it.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Car is updated successful.",
            content = { @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CarDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found and cannot be updated.",
                    content = { @Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid car.",
                    content = { @Content(mediaType = APPLICATION_JSON_VALUE)})})
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(value = "/update", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CarDTO> updateCar(@Parameter(description = "car that will be updated")
                                                @Valid @RequestBody CarDTO carDTO) throws NoSuchCarException {

        log.info("Http method - Put, update car {}", carDTO.toString());
        log.info("Car id : {}", carDTO.getId());

        Car car = dtoToCarEntity(carDTO);
        carDTO = carEntityToDTO(carService.update(car));

        return ResponseEntity.ok().body(carDTO);
    }

    @Operation(summary = "Delete car and their image.",
            description = "For valid response try integer IDs with positive integer value. " +
                    "Negative or non-integer values will generate API errors")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Deletes the car and their image file.",
            content = { @Content(mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = CarDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Car not found and cannot be deleted.",
                    content = { @Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid car ID or image file name.",
                    content = { @Content(mediaType = APPLICATION_JSON_VALUE)})
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/remove/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<CarDTO> removeCar(@Parameter(description = "ID of car that needs to be deleted.")
                                                @PathVariable Long id,
                                            @Parameter(description = "name of mage file that has car needs to be deleted")
                                            @RequestParam String imageName) throws NoSuchCarException {

        log.info("Http method - Delete, delete car with id {}", id);

        carService.deleteById(id);
        ResponseEntity<CarDTO> response = ResponseEntity.ok().build();

        if(!imageName.equals("default-car-image")){
            log.info("Http method - Delete, delete image with name {}", imageName);
            this.amazonClient.deleteFileFromS3Bucket(imageName);
        }

        return response;
    }
}