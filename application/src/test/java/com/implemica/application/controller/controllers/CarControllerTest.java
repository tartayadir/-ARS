package com.implemica.application.controller.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.implemica.controller.controllers.CarController;
import com.implemica.controller.exceptions.NoSuchCarException;
import com.implemica.controller.handlers.ValidationHandler;
import com.implemica.controller.service.amazonS3.AmazonClient;
import com.implemica.controller.service.car.service.CarService;
import com.implemica.model.car.dto.CarDTO;
import com.implemica.model.car.entity.Car;
import com.implemica.model.car.entity.CarBodyTypes;
import com.implemica.model.car.entity.CarBrands;
import com.implemica.model.car.entity.TransmissionBoxTypes;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.implemica.controller.utils.ConverterDTO.carEntityToDTO;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static utils.spring.StringUtils.generateRandomString;
import static utils.spring.AuthTestUtils.getToken;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class CarControllerTest {

    @Autowired
    private AmazonClient amazonClient;

    @Autowired
    private AmazonS3 s3client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private static CarService carService;

    private static List<Car> cars;

    private static List<CarDTO> carDTOs;

    private static Car car;

    private static Car carForReturn;

    private static CarDTO carDTO;

    private static String token;

    public static Long id;

    private static MockMvc mockMvc;

    @Autowired
    private CarController carController;

    @Autowired
    private ValidationHandler validationHandler;

    @Autowired
    private Filter springSecurityFilterChain;

    @SneakyThrows
    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.
                standaloneSetup(carController).
                setControllerAdvice(validationHandler).
                addFilters(springSecurityFilterChain).
                build();

        short year1 = 2019;
        short year2 = 2009;
        short year3 = 2021;

        cars = List.of(
                Car.builder().
                        id(1L).
                        brand(CarBrands.PORSCHE).
                        model("Model 11").
                        carBodyTypes(CarBodyTypes.CONVERTIBLE).
                        year(year1).
                        transmissionBoxTypes(TransmissionBoxTypes.ROBOTIC).
                        engineCapacity(3.4).
                        shortDescription("Short description 1").
                        fullDescription("Full description 1").
                        additionalOptions(List.of("Option 1", "Option 2", "option 3")).
                        imageFileName("Image file 1").
                        build(),
                Car.builder().
                        id(2L).
                        brand(CarBrands.NISSAN).
                        model("Model 1").
                        carBodyTypes(CarBodyTypes.PICKUP).
                        year(year2).
                        transmissionBoxTypes(TransmissionBoxTypes.MECHANICAL).
                        engineCapacity(5.4).
                        shortDescription("Short description 1").
                        fullDescription("Full description 1").
                        additionalOptions(List.of("Option 1", "Option 2", "option 3")).
                        imageFileName("Image file 1").
                        build(),
                Car.builder().
                        id(3L).
                        brand(CarBrands.ROLLS_ROYCE).
                        model("Model 1").
                        carBodyTypes(CarBodyTypes.HATCHBACK).
                        year(year3).
                        transmissionBoxTypes(TransmissionBoxTypes.VARIATIONAL).
                        engineCapacity(1.3).
                        shortDescription("Short description 1").
                        fullDescription("Full description 1").
                        additionalOptions(List.of("Option 1", "Option 2", "option 3")).
                        imageFileName("Image file 1").
                        build()
        );

        carDTOs = List.of(
                CarDTO.builder().
                        id(1L).
                        brand("PORSCHE").
                        model("Model 11").
                        carBodyTypes("CONVERTIBLE").
                        year(year1).
                        transmissionBoxTypes("ROBOTIC").
                        engineCapacity(3.4).
                        shortDescription("Short description 1").
                        fullDescription("Full description 1").
                        additionalOptions(List.of("Option 1", "Option 2", "option 3")).
                        imageFileName("Image file 1").
                        build(),
                CarDTO.builder().
                        id(2L).
                        brand("NISSAN").
                        model("Model 1").
                        carBodyTypes("PICKUP").
                        year(year2).
                        transmissionBoxTypes("MECHANICAL").
                        engineCapacity(5.4).
                        shortDescription("Short description 1").
                        fullDescription("Full description 1").
                        additionalOptions(List.of("Option 1", "Option 2", "option 3")).
                        imageFileName("Image file 1").
                        build(),
                CarDTO.builder().
                        id(3L).
                        brand("ROLLS_ROYCE").
                        model("Model 1").
                        carBodyTypes("HATCHBACK").
                        year(year3).
                        transmissionBoxTypes("VARIATIONAL").
                        engineCapacity(1.3).
                        shortDescription("Short description 1").
                        fullDescription("Full description 1").
                        additionalOptions(List.of("Option 1", "Option 2", "option 3")).
                        imageFileName("Image file 1").
                        build()
        );

        short year = 2012;
        car = Car.builder().
                id(10L).
                brand(CarBrands.ALFA).
                model("Model 1").
                carBodyTypes(CarBodyTypes.SPORTS_CAR).
                year(year).
                transmissionBoxTypes(TransmissionBoxTypes.AUTOMATIC).
                engineCapacity(5.4).
                shortDescription("Short description 1 Short description 1 Short description 1Short dddddd " +
                        "Short description 1 Short description 1 Short description 1Short description fds " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        "Short description 1 Short description 1Short description 1 Short description 1 S "
                ).
                fullDescription( "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1"
                ).
                additionalOptions(List.of("Option 1", "Option 2", "option 3")).
                imageFileName("Image file 1").
                build();

        year = 2019;
        carDTO = CarDTO.builder().
                id(10L).
                brand("ALFA").
                model("Model 1").
                carBodyTypes("SPORTS_CAR").
                year(year).
                transmissionBoxTypes("AUTOMATIC").
                engineCapacity(5.4).
                shortDescription("Short description 1 Short description 1 Short description 1Short dddddd " +
                        "Short description 1 Short description 1 Short description 1Short description fds " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        "Short description 1 Short description 1Short description 1 Short description 1 S "
                ).
                fullDescription( "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1"
                 ).
                additionalOptions(List.of("Option 1", "Option 2", "option 3")).
                imageFileName("Image file 1").
                build();

        carForReturn = Car.builder().
                id(10L).
                brand(CarBrands.ALFA).
                model("Model 1").
                carBodyTypes(CarBodyTypes.SPORTS_CAR).
                year(year).
                transmissionBoxTypes(TransmissionBoxTypes.AUTOMATIC).
                engineCapacity(5.4).
                shortDescription("Short description 1 Short description 1 Short description 1Short dddddd " +
                        "Short description 1 Short description 1 Short description 1Short description fds " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        "Short description 1 Short description 1Short description 1 Short description 1 S "
                ).
                fullDescription( "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1"
                ).
                additionalOptions(List.of("Option 1", "Option 2", "option 3")).
                imageFileName("Image file 1").
                build();

        carDTO = CarDTO.builder().
                id(10L).
                brand("ALFA").
                model("Model 1").
                carBodyTypes("SPORTS_CAR").
                year(year).
                transmissionBoxTypes("AUTOMATIC").
                engineCapacity(5.4).
                shortDescription("Short description 1 Short description 1 Short description 1Short dddddd " +
                        "Short description 1 Short description 1 Short description 1Short description fds " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        "Short description 1 Short description 1Short description 1 Short description 1 S "
                ).
                fullDescription( "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1" +
                        "Full description 1 Full description 1 Full description 1Full description 1"
                ).
                additionalOptions(List.of("Option 1", "Option 2", "option 3")).
                imageFileName("Image file 1").
                build();

        token = getToken();
        id = 10L;
    }

    @Test
    void updateCar() throws Exception {

        when(carService.update(any(Car.class))).thenReturn(carForReturn);
        String requestJSON = objectMapper.writeValueAsString(carDTO);
        String responseJSON = objectMapper.writeValueAsString(carDTO);
        System.out.println(requestJSON);

        mockMvc.perform(put("/car-catalog/update").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        characterEncoding("utf-8").
                        accept(APPLICATION_JSON).
                        header(AUTHORIZATION, token)
                ).
                andExpect(status().isOk()).
                andExpect(content().json(responseJSON)).
                andReturn();

        mockMvc.perform(put("/car-catalog/update").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        characterEncoding("utf-8").
                        accept(APPLICATION_JSON)
                ).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(1)).update(any(Car.class));
    }

    @Test
    void updateCar_cannot_find_car() throws Exception {

        when(carService.update(any(Car.class))).
                thenThrow( new NoSuchCarException(format("Cannot find and update car with id %d", car.getId())));
        String requestJSON = objectMapper.writeValueAsString(carDTO);
        String exceptedErrorMassage = format("Cannot find and update car with id %d", carDTO.getId());

        mockMvc.perform(put("/car-catalog/update").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        characterEncoding("utf-8").
                        accept(APPLICATION_JSON).
                        header(AUTHORIZATION, token)
                ).
                andExpect(status().isNotFound()).
                andDo(System.out::println).
                andExpect(jsonPath("$.error_message").
                        value(exceptedErrorMassage)).
                andReturn();

        mockMvc.perform(put("/car-catalog/update").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        characterEncoding("utf-8").
                        accept(APPLICATION_JSON)
                ).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(1)).update(any(Car.class));
    }

    @Test
    void updateCar_with_null_id() throws Exception {

        when(carService.update(any(Car.class))).thenThrow(new IllegalArgumentException("Id cannot be null"));
        String requestJSON = objectMapper.writeValueAsString(carDTO);
        String exceptedErrorMassage = "Id cannot be null";

        mockMvc.perform(put("/car-catalog/update").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        characterEncoding("utf-8").
                        accept(APPLICATION_JSON).
                        header(AUTHORIZATION, token)
                ).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.error_message").
                        value(exceptedErrorMassage)).
                andReturn();

        mockMvc.perform(put("/car-catalog/update").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        characterEncoding("utf-8").
                        accept(APPLICATION_JSON)
                ).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(1)).update(any(Car.class));
    }

    @Test
    void updateCar_null_car() throws Exception {

        String requestJSON = "{}";

        mockMvc.perform(put("/car-catalog/update").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        accept(APPLICATION_JSON).
                        header(AUTHORIZATION, token)
                ).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.brand").
                        value("Brand is required and cannot be empty or null.")).
                andExpect(jsonPath("$.model").
                        value("Model is required and cannot be empty or null.")).
                andExpect(jsonPath("$.year").
                        value("Produce year is required and cannot be empty or null.")).
                andExpect(jsonPath("$.imageFileName").
                        value("Image file name is required and cannot be empty or null.")).
                andExpect(jsonPath("$.transmissionBoxTypes").
                        value("Transmission box type is required and cannot be empty or null.")).
                andExpect(jsonPath("$.carBodyTypes").
                        value("Car body type is required and cannot be empty or null.")).
                andExpect(jsonPath("$.engineCapacity").
                        value("Engine capacity is required.")).
                andReturn();

        mockMvc.perform(put("/car-catalog/update").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        accept(APPLICATION_JSON)
                ).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(0)).update(any(Car.class));
    }

    @Test
    void addCar() throws Exception {

        when(carService.save(any(Car.class))).thenReturn(carForReturn);
        String requestJSON = objectMapper.writeValueAsString(carDTO);
        String responseJSON = objectMapper.writeValueAsString(carDTO);

        mockMvc.perform(post("/car-catalog/add").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        characterEncoding("utf-8").
                        accept(APPLICATION_JSON).
                        header(AUTHORIZATION, token)
                ).
                andExpect(status().isCreated()).
                andExpect(content().json(responseJSON)).
                andReturn();

        mockMvc.perform(post("/car-catalog/add").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        characterEncoding("utf-8").
                        accept(APPLICATION_JSON)
                ).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(1)).save(any(Car.class));
    }

    @Test
    void addCar_null_car() throws Exception {

        String requestJSON = "{}";

        mockMvc.perform(post("/car-catalog/add").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        accept(APPLICATION_JSON).
                        header(AUTHORIZATION, token)
                ).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath("$.brand").
                        value("Brand is required and cannot be empty or null.")).
                andExpect(jsonPath("$.model").
                        value("Model is required and cannot be empty or null.")).
                andExpect(jsonPath("$.year").
                        value("Produce year is required and cannot be empty or null.")).
                andExpect(jsonPath("$.imageFileName").
                        value("Image file name is required and cannot be empty or null.")).
                andExpect(jsonPath("$.transmissionBoxTypes").
                        value("Transmission box type is required and cannot be empty or null.")).
                andExpect(jsonPath("$.carBodyTypes").
                        value("Car body type is required and cannot be empty or null.")).
                andExpect(jsonPath("$.engineCapacity").
                        value("Engine capacity is required.")).
                andReturn();

        verify(carService, times(0)).save(any(Car.class));
    }

    @Test
    void addCar_invalid_brand() throws Exception {

        String exceptedMassage = "Brand is required and cannot be empty or null.";
        String fieldName = "brand";

        carDTO.setBrand(null);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        exceptedMassage = "Car brand is invalid.";

        carDTO.setBrand("wfjfowjfojwofijowij");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setBrand("1ljk12j2p");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setBrand("+1-221=-23DSWDwd");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setBrand("wfjfowdwkdo2-=201==21wjfojwofijowij");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_model() throws Exception {

        String exceptedMassage = "Model is required and cannot be empty or null.";
        String fieldName = "model";

        carDTO.setModel(null);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        exceptedMassage = "Model must be greater than 2 and less then 30.";

        carDTO.setModel("w");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setModel("a");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setModel(generateRandomString(41));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setModel(generateRandomString(42));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setModel(generateRandomString(50));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setModel(generateRandomString(120));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        exceptedMassage = "Model must not contain anything other than letters.";

        carDTO.setModel("1dkpkwpdkp wdppodk wdwdwd");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setModel("2084084 dkpkwpdkp wdppodk wdwdwd");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setModel("430dkpkwpdkp wdppodk wdwdwd");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setModel("!dkpkwpdkp wdppodk wdwdwd");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setModel("+dkpkwpdkp wdppodk wdwdwd");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setModel("@dkpkwpdkp wdppodk wdwdwd");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_car_body_type() throws Exception {

        String exceptedMassage = "Car body type is required and cannot be empty or null.";
        String fieldName = "carBodyTypes";

        carDTO.setCarBodyTypes(null);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        exceptedMassage = "Car bode type is invalid.";

        carDTO.setCarBodyTypes("w");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("п-вкпзушпзцхкзщхущ0-2щу230=-09232w");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("Фгвшш");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("gosgoeut395239542d");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("audi");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("wt34");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("au+`-13r4=tgfdi");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_produce_year() throws Exception {

        String exceptedMassage = "Produce year is required and cannot be empty or null.";
        String fieldName = "year";

        carDTO.setYear(null);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        exceptedMassage = "Prodice year must be greater than 1920 and less than 2022.";

        short year = 1;
        carDTO.setYear(year);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        year = 0;
        carDTO.setYear(year);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        year = -1;
        carDTO.setYear(year);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        year = 1_918;
        carDTO.setYear(year);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        year = 1_919;
        carDTO.setYear(year);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        year = 2023;
        carDTO.setYear(year);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        year = 2024;
        carDTO.setYear(year);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        year = 32_000;
        carDTO.setYear(year);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        year = -32_000;
        carDTO.setYear(year);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_transmission_box_type() throws Exception {

        String exceptedMassage = "Transmission box type is required and cannot be empty or null.";
        String fieldName = "transmissionBoxTypes";

        carDTO.setTransmissionBoxTypes(null);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        exceptedMassage = "Transmission box type is invalid.";

        carDTO.setTransmissionBoxTypes("gre;gjepk");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("2394820jlkfoif-scaq=e33`");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("wjgoewjgto3jpt");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("gre;gjepk");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("t94-poedok02413");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("309282opfkwe9r224]]k");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("4-03-0kopf3g-043itgp4igf-e0f[spf");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("+_+_!#_#$#+|#$}%4");
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_engine_capacity() throws Exception {

        String  exceptedMassage = "Engine capacity must be positive number or 0.";
        String fieldName = "engineCapacity";

        carDTO.setEngineCapacity(-0.1);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setEngineCapacity(-1.0);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setEngineCapacity(-424.35);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setEngineCapacity(-6440699.53545456);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        exceptedMassage = "Engine capacity must be less then 15,0.";

        carDTO.setEngineCapacity(15.1);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setEngineCapacity(15.2);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setEngineCapacity(424.35);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setEngineCapacity(6440699.53545456);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_full_description() throws Exception {

        String exceptedMassage = "Full description must be less then 5000.";
        String fieldName = "fullDescription";

        carDTO.setFullDescription(generateRandomString(5001));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setFullDescription(generateRandomString(5002));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setFullDescription(generateRandomString(20_111));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        exceptedMassage = "The full description should not contain anything but a word," +
                " numbers and punctuation marks, also cannot start with number or punctuation mark.";

        carDTO.setFullDescription("!" + generateRandomString(500));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setFullDescription("23" + generateRandomString(500));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setFullDescription("5" + generateRandomString(500));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setFullDescription("+" + generateRandomString(500));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setFullDescription(")" + generateRandomString(500));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_short_description() throws Exception {

        String exceptedMassage = "Short description must be less then 1000.";
        String fieldName = "shortDescription";

        carDTO.setShortDescription(generateRandomString(1001));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setShortDescription(generateRandomString(1002));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setShortDescription(generateRandomString(5_029));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        exceptedMassage = "The short description should not contain anything but a word," +
                " numbers and punctuation marks, also cannot start with number or punctuation mark.";

        carDTO.setShortDescription("!" + generateRandomString(500));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setShortDescription("23" + generateRandomString(500));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setShortDescription("5" + generateRandomString(500));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setShortDescription("+" + generateRandomString(500));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setShortDescription(")" + generateRandomString(500));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_image_file_name() throws Exception {

        String exceptedMassage = "Image file name is required and cannot be empty or null.";
        String fieldName = "imageFileName";

        carDTO.setImageFileName(null);
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        exceptedMassage = "Image file name must be greater than 10 and less then 150.";

        carDTO.setImageFileName(generateRandomString(9));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setImageFileName(generateRandomString(8));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setImageFileName(generateRandomString(151));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setImageFileName(generateRandomString(151));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setImageFileName(generateRandomString(3));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setImageFileName(generateRandomString(374));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);

        carDTO.setImageFileName(generateRandomString(5_029));
        assertErrorsMassageForInvalidCarFields(fieldName, exceptedMassage);
    }

//    @Test
//    void addCar_invalid_additional_options() throws Exception {
//
//        String exceptedMassage = "Addition option cannot be null or empty";
//        String fieldName = "additionalOptions[0]";
//
//        List<String> options = new ArrayList<>();
//        options.add(null);
//
//        carDTO.setAdditionalOptions(options);
//        assertErrorsMassageForInvalidAddOptionsCarFields(fieldName, exceptedMassage);
//
//        exceptedMassage = "Addition option must be greater than 2 and less then 20.";
//
//        carDTO.setAdditionalOptions(List.of(generateRandomString(1)));
//        assertErrorsMassageForInvalidAddOptionsCarFields(fieldName, exceptedMassage);
//
//        carDTO.setAdditionalOptions(List.of(generateRandomString(21)));
//        assertErrorsMassageForInvalidAddOptionsCarFields(fieldName, exceptedMassage);
//
//        exceptedMassage = "Option must not contain anything other than " +
//                "numbers and letters or only numbers, also cannot start with number.";
//
//        carDTO.setAdditionalOptions(List.of("+_-34-34"));
//        assertErrorsMassageForInvalidAddOptionsCarFields(fieldName, exceptedMassage);
//
//        carDTO.setAdditionalOptions(List.of("3483-4!@+!2"));
//        assertErrorsMassageForInvalidAddOptionsCarFields(fieldName, exceptedMassage);
//
//        carDTO.setAdditionalOptions(List.of("420423_@~1334"));
//        assertErrorsMassageForInvalidAddOptionsCarFields(fieldName, exceptedMassage);
//    }

    @Test
    public void getAllCars() throws Exception {

        when(carService.findAll()).thenReturn(cars);
        String exceptedJSON = objectMapper.writeValueAsString(carDTOs);

        mockMvc.perform(get("/car-catalog/all-cars")).
                andExpect(status().isOk()).
                andExpect(content().string(containsString(exceptedJSON))).
                andReturn();

        verify(carService, times(1)).findAll();
    }

    @Test
    void getCar() throws Exception {

        car.setId(id);
        when(carService.findById(id)).thenReturn(car);
        CarDTO exceptedCar = carEntityToDTO(car);
        String exceptedJSON = objectMapper.writeValueAsString(exceptedCar);

        mockMvc.perform(get(format("/car-catalog/details/%d", id))).
                andExpect(status().isOk()).
                andExpect(content().string(containsString(exceptedJSON))).
                andReturn();

        verify(carService, times(1)).findById(id);
    }

    @Test
    void getCar_with_valid_id_not_found_car() throws Exception {

        when(carService.findById(id)).thenThrow(new NoSuchCarException(format("Cannot find car with id %d", id)));
        String exceptedJSON = format("Cannot find car with id %d", id);

        mockMvc.perform(get(format("/car-catalog/details/%d", id))).
                andExpect(status().isNotFound()).
                andExpect(content().string(containsString(exceptedJSON))).
                andReturn();

        verify(carService, times(1)).findById(id);
    }

    @Test
    void getCar_with_null_id() throws Exception {

        doThrow(NoSuchCarException.class).
                when(carService).
                findById(id);

        mockMvc.perform(get(format("/car-catalog/details/%d", id))).
                andExpect(status().isNotFound()).
                andReturn();

        verify(carService, times(1)).findById(id);
    }

    @Test
    void removeCar() throws Exception {

        String fileName = "FileName";
        MockMultipartFile file = new MockMultipartFile(
                "imageFile", fileName + ".jpeg",
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        amazonClient.uploadFileTos3bucket(fileName, file);
        doNothing().when(carService).deleteById(id);

        S3Object s3Object = s3client.getObject(bucketName, fileName);

        assertEquals(fileName, s3Object.getKey());

        mockMvc.perform(delete(format("/car-catalog/remove/%d", id))
                        .param("imageName", fileName)
                        .header(AUTHORIZATION, token)).
                andExpect(status().isOk());

        assertThatThrownBy(() -> s3client.getObject(bucketName, fileName)).
                isInstanceOf(AmazonS3Exception.class);

        mockMvc.perform(delete(format("/car-catalog/remove/%d", id))).
                andExpect(status().isForbidden());

        verify(carService, times(1)).deleteById(id);
    }

    @Test
    void removeCar_default_image() throws Exception {

        String fileName = "default-car-image";

        S3Object s3Object = s3client.getObject(bucketName, fileName);
        assertEquals(fileName, s3Object.getKey());

        mockMvc.perform(delete(format("/car-catalog/remove/%d", id))
                        .param("imageName", fileName)
                        .header(AUTHORIZATION, token)).
                andExpect(status().isOk());

        s3Object = s3client.getObject(bucketName, fileName);
        assertEquals(fileName, s3Object.getKey());

        mockMvc.perform(delete(format("/car-catalog/remove/%d", id))).
                andExpect(status().isForbidden());

        verify(carService, times(1)).deleteById(id);
    }

    @Test
    void removeCar_with_valid_id_not_found_car() throws Exception {

        doThrow(new NoSuchCarException(format("Cannot find car with id %d", id))).
                when(carService).
                deleteById(id);
        String exceptedJSON = format("Cannot find car with id %d", id);

        mockMvc.perform(delete(format("/car-catalog/remove/%d", id))
                        .header(AUTHORIZATION, token)
                        .param("imageName", "")).
                andExpect(status().isNotFound()).
                andExpect(content().string(containsString(exceptedJSON))).
                andReturn();

        mockMvc.perform(delete(format("/car-catalog/remove/%d", id))).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(1)).deleteById(id);
    }

    @Test
    void removeCar_with_null_id() throws Exception {

        doThrow(NoSuchCarException.class).
                when(carService).
                deleteById(id);

        mockMvc.perform(delete(format("/car-catalog/remove/%d", id))
                        .header(AUTHORIZATION, token)
                        .param("imageName", "")).
                andExpect(status().isNotFound()).
                andReturn();

        mockMvc.perform(delete(format("/car-catalog/remove/%d", id))).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(1)).deleteById(id);
    }

    private static void assertErrorsMassageForInvalidAddOptionsCarFields(String jsonInvalidFieldName,
                                                                         String exceptedErrorMassage) throws Exception {

        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put(jsonInvalidFieldName, exceptedErrorMassage);

        String requestJSON = objectMapper.writeValueAsString(carDTO);
        String responseJSON = objectMapper.writeValueAsString(errorMessages);

        mockMvc.perform(post("/car-catalog/add").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        accept(APPLICATION_JSON).
                        header(AUTHORIZATION, token)
                ).
                andExpect(status().isBadRequest()).
                andDo(result -> System.out.println(result.getResponse().getContentAsString())).
                andExpect(content().json(responseJSON)).
                andReturn();

        mockMvc.perform(post("/car-catalog/add").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        accept(APPLICATION_JSON)
                ).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(0)).save(any(Car.class));

        mockMvc.perform(put("/car-catalog/update").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        accept(APPLICATION_JSON)
                ).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(0)).update(any(Car.class));
    }

    private static void assertErrorsMassageForInvalidCarFields(String jsonInvalidFieldName,
                                                               String exceptedErrorMassage) throws Exception {

        String requestJSON = objectMapper.writeValueAsString(carDTO);

        mockMvc.perform(post("/car-catalog/add").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        accept(APPLICATION_JSON).
                        header(AUTHORIZATION, token)
                ).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath(format("%s", jsonInvalidFieldName)).
                        value(exceptedErrorMassage)).
                andReturn();

        mockMvc.perform(post("/car-catalog/add").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        accept(APPLICATION_JSON)
                ).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(0)).save(any(Car.class));

        mockMvc.perform(put("/car-catalog/update").
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        accept(APPLICATION_JSON)
                ).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(0)).update(any(Car.class));
    }
}