package com.implemica.application.controller.controllers;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.implemica.controller.controllers.CarController;
import com.implemica.controller.exceptions.NoSuchCarException;
import com.implemica.controller.handlers.ValidationHandler;
import com.implemica.controller.service.amazonS3.AmazonClient;
import com.implemica.controller.service.auth.service.AuthService;
import com.implemica.controller.service.car.service.CarService;
import com.implemica.model.car.dto.CarDTO;
import com.implemica.model.car.entity.Car;
import com.implemica.model.car.enums.CarBodyType;
import com.implemica.model.car.enums.CarBrand;
import com.implemica.model.car.enums.TransmissionBoxType;
import com.utils.spring.AuthTestUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.Filter;
import java.util.List;
import java.util.stream.Collectors;

import static com.implemica.model.car.dto.CarDTO.toDTO;
import static com.utils.CarsUtils.*;
import static com.utils.spring.StringUtils.generateRandomString;
import static com.utils.spring.URIUtils.*;
import static java.lang.Long.MAX_VALUE;
import static java.lang.Long.MIN_VALUE;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration-test")
@Slf4j
class CarControllerTest {

    private static AmazonClient amazonClient;

    private static AmazonS3 s3client;

    private static String bucketName;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private static CarService carService;

    private static List<CarDTO> carDTOs;

    private static Car car;

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

    private static AuthTestUtils authTestUtils;

    @SneakyThrows
    @BeforeEach
    void setUp() {

        log.info("" + System.getProperty("spring.datasource.password"));

        bucketName = AmazonClient.getBucketName();

        mockMvc = MockMvcBuilders.
                standaloneSetup(carController).
                setControllerAdvice(validationHandler).
                addFilters(springSecurityFilterChain).
                build();

        short year1 = 2019;
        short year2 = 2009;
        short year3 = 2021;

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
                        imageFileId("Image file 1").
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
                        imageFileId("Image file 1").
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
                        imageFileId("Image file 1").
                        build()
        );

        short year = 2012;
        car = Car.builder().
                id(10L).
                brand(CarBrand.ALFA).
                model("Model 1").
                carBodyTypes(CarBodyType.SPORTS_CAR).
                year(year).
                transmissionBoxTypes(TransmissionBoxType.AUTOMATIC).
                engineCapacity(5.4).
                shortDescription("Short description 1 Short description 1 Short description 1Short dddddd " +
                        "Short description 1 Short description 1 Short description 1Short description fds " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        " 1Short description 1 Short description 1 Short description 1Short description 1 " +
                        "Short description 1 Short description 1Short description 1 Short description 1 S "
                ).
                fullDescription("Full description 1 Full description 1 Full description 1Full description 1" +
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
                fullDescription("Full description 1 Full description 1 Full description 1Full description 1" +
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
                imageFileId("Image file 1").
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
                fullDescription("Full description 1 Full description 1 Full description 1Full description 1" +
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
                imageFileId("Image file 1").
                build();

        token = authTestUtils.getAdminToken();
        id = 10L;
    }

    @Test
    void addCar() {

        checkAddCar(carDTO);
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
        checkAddCar(generateRandomCarDto());
    }

    @Test
    void addCar_null_car() throws Exception {

        String requestJSON = "{}";

        mockMvc.perform(post(getAddCarUri()).
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
                andExpect(jsonPath("$.imageFileId").
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
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Car brand is invalid.";

        carDTO.setBrand("wfjfowjfojwofijowij");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setBrand("1ljk12j2p");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setBrand("+1-221=-23DSWDwd");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setBrand("wfjfowdwkdo2-=201==21wjfojwofijowij");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_model() throws Exception {

        String exceptedMassage = "Model is required and cannot be empty or null.";
        String fieldName = "model";

        carDTO.setModel(null);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Model must be greater than 2 and less then 30.";

        carDTO.setModel("w");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel("a");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel(generateRandomString(41));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel(generateRandomString(42));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel(generateRandomString(50));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel(generateRandomString(120));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Model must not contain anything other than letters.";

        carDTO.setModel("1dkpkwpdkp wdppodk wdwdwd");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel("2084084 dkpkwpdkp wdppodk wdwdwd");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel("430dkpkwpdkp wdppodk wdwdwd");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel("!dkpkwpdkp wdppodk wdwdwd");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel("+dkpkwpdkp wdppodk wdwdwd");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel("@dkpkwpdkp wdppodk wdwdwd");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_car_body_type() throws Exception {

        String exceptedMassage = "Car body type is required and cannot be empty or null.";
        String fieldName = "carBodyTypes";

        carDTO.setCarBodyTypes(null);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Car bode type is invalid.";

        carDTO.setCarBodyTypes("w");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("п-вкпзушпзцхкзщхущ0-2щу230=-09232w");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("Фгвшш");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("gosgoeut395239542d");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("audi");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("wt34");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("au+`-13r4=tgfdi");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_produce_year() throws Exception {

        String exceptedMassage = "Produce year is required and cannot be empty or null.";
        String fieldName = "year";

        carDTO.setYear(null);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Prodice year must be greater than 1920 and less than 2022.";

        short year = 1;
        carDTO.setYear(year);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = 0;
        carDTO.setYear(year);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = -1;
        carDTO.setYear(year);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = 1_918;
        carDTO.setYear(year);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = 1_919;
        carDTO.setYear(year);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = 2023;
        carDTO.setYear(year);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = 2024;
        carDTO.setYear(year);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = 32_000;
        carDTO.setYear(year);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = -32_000;
        carDTO.setYear(year);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_transmission_box_type() throws Exception {

        String exceptedMassage = "Transmission box type is required and cannot be empty or null.";
        String fieldName = "transmissionBoxTypes";

        carDTO.setTransmissionBoxTypes(null);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Transmission box type is invalid.";

        carDTO.setTransmissionBoxTypes("gre;gjepk");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("2394820jlkfoif-scaq=e33`");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("wjgoewjgto3jpt");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("gre;gjepk");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("t94-poedok02413");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("309282opfkwe9r224]]k");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("4-03-0kopf3g-043itgp4igf-e0f[spf");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("+_+_!#_#$#+|#$}%4");
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_engine_capacity() throws Exception {

        String exceptedMassage = "Engine capacity must be positive number or 0.";
        String fieldName = "engineCapacity";

        carDTO.setEngineCapacity(-0.1);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setEngineCapacity(-1.0);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setEngineCapacity(-424.35);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setEngineCapacity(-6440699.53545456);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Engine capacity must be less then 15,0.";

        carDTO.setEngineCapacity(15.1);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setEngineCapacity(15.2);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setEngineCapacity(424.35);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setEngineCapacity(6440699.53545456);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_full_description() throws Exception {

        String exceptedMassage = "Full description must be less then 5000.";
        String fieldName = "fullDescription";

        carDTO.setFullDescription(generateRandomString(5001));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setFullDescription(generateRandomString(5002));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setFullDescription(generateRandomString(20_111));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "The full description should not contain anything but a word," +
                " numbers and punctuation marks, also cannot start with number or punctuation mark.";

        carDTO.setFullDescription("!" + generateRandomString(500));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setFullDescription("23" + generateRandomString(500));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setFullDescription("5" + generateRandomString(500));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setFullDescription("+" + generateRandomString(500));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setFullDescription(")" + generateRandomString(500));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_short_description() throws Exception {

        String exceptedMassage = "Short description must be less then 1000.";
        String fieldName = "shortDescription";

        carDTO.setShortDescription(generateRandomString(1001));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setShortDescription(generateRandomString(1002));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setShortDescription(generateRandomString(5_029));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "The short description should not contain anything but a word," +
                " numbers and punctuation marks, also cannot start with number or punctuation mark.";

        carDTO.setShortDescription("!" + generateRandomString(500));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setShortDescription("23" + generateRandomString(500));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setShortDescription("5" + generateRandomString(500));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setShortDescription("+" + generateRandomString(500));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setShortDescription(")" + generateRandomString(500));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void addCar_invalid_image_file_name() throws Exception {

        String exceptedMassage = "Image file name is required and cannot be empty or null.";
        String fieldName = "imageFileId ";

        carDTO.setImageFileId(null);
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Image file name must be greater than 10 and less then 150.";

        carDTO.setImageFileId(generateRandomString(9));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setImageFileId(generateRandomString(8));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setImageFileId(generateRandomString(151));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setImageFileId(generateRandomString(151));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setImageFileId(generateRandomString(3));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setImageFileId(generateRandomString(374));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setImageFileId(generateRandomString(5_029));
        checkAddInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void updateCar() {

        checkUpdateCar(carDTO);
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
        checkUpdateCar(generateRandomCarDto());
    }

    @Test
    void updateCar_cannot_find_car() {

        Exception serviceException =
                new NoSuchCarException(format("Cannot find and update car with id %d", car.getId()));

        checkErrorUpdateCar(carDTO, serviceException, format("Cannot find and update car with id %d", carDTO.getId()),
                NOT_FOUND);
    }

    @Test
    void updateCar_with_null_id() {

        Exception serviceException = new IllegalArgumentException("Id cannot be null");

        checkErrorUpdateCar(carDTO, serviceException, "Id cannot be null", BAD_REQUEST);
    }

    @Test
    void updateCar_null_car() throws Exception {

        String requestJSON = "{}";

        mockMvc.perform(put(getUpdateCarUri()).
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
                andExpect(jsonPath("$.imageFileId").
                        value("Image file name is required and cannot be empty or null.")).
                andExpect(jsonPath("$.transmissionBoxTypes").
                        value("Transmission box type is required and cannot be empty or null.")).
                andExpect(jsonPath("$.carBodyTypes").
                        value("Car body type is required and cannot be empty or null.")).
                andExpect(jsonPath("$.engineCapacity").
                        value("Engine capacity is required.")).
                andReturn();

        mockMvc.perform(put(getUpdateCarUri()).
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        accept(APPLICATION_JSON)
                ).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(0)).update(any(Car.class));
    }

    @Test
    void updateCar_invalid_brand() throws Exception {

        String exceptedMassage = "Brand is required and cannot be empty or null.";
        String fieldName = "brand";

        carDTO.setBrand(null);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Car brand is invalid.";

        carDTO.setBrand("wfjfowjfojwofijowij");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setBrand("1ljk12j2p");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setBrand("+1-221=-23DSWDwd");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setBrand("wfjfowdwkdo2-=201==21wjfojwofijowij");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void updateCar_invalid_model() throws Exception {

        String exceptedMassage = "Model is required and cannot be empty or null.";
        String fieldName = "model";

        carDTO.setModel(null);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Model must be greater than 2 and less then 30.";

        carDTO.setModel("w");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel("a");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel(generateRandomString(41));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel(generateRandomString(42));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel(generateRandomString(50));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel(generateRandomString(120));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Model must not contain anything other than letters.";

        carDTO.setModel("1dkpkwpdkp wdppodk wdwdwd");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel("2084084 dkpkwpdkp wdppodk wdwdwd");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel("430dkpkwpdkp wdppodk wdwdwd");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel("!dkpkwpdkp wdppodk wdwdwd");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel("+dkpkwpdkp wdppodk wdwdwd");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setModel("@dkpkwpdkp wdppodk wdwdwd");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void updateCar_invalid_car_body_type() throws Exception {

        String exceptedMassage = "Car body type is required and cannot be empty or null.";
        String fieldName = "carBodyTypes";

        carDTO.setCarBodyTypes(null);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Car bode type is invalid.";

        carDTO.setCarBodyTypes("w");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("п-вкпзушпзцхкзщхущ0-2щу230=-09232w");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("Фгвшш");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("gosgoeut395239542d");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("audi");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("wt34");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setCarBodyTypes("au+`-13r4=tgfdi");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void updateCar_invalid_produce_year() throws Exception {

        String exceptedMassage = "Produce year is required and cannot be empty or null.";
        String fieldName = "year";

        carDTO.setYear(null);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Prodice year must be greater than 1920 and less than 2022.";

        short year = 1;
        carDTO.setYear(year);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = 0;
        carDTO.setYear(year);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = -1;
        carDTO.setYear(year);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = 1_918;
        carDTO.setYear(year);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = 1_919;
        carDTO.setYear(year);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = 2023;
        carDTO.setYear(year);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = 2024;
        carDTO.setYear(year);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = 32_000;
        carDTO.setYear(year);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        year = -32_000;
        carDTO.setYear(year);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void updateCar_invalid_transmission_box_type() throws Exception {

        String exceptedMassage = "Transmission box type is required and cannot be empty or null.";
        String fieldName = "transmissionBoxTypes";

        carDTO.setTransmissionBoxTypes(null);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Transmission box type is invalid.";

        carDTO.setTransmissionBoxTypes("gre;gjepk");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("2394820jlkfoif-scaq=e33`");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("wjgoewjgto3jpt");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("gre;gjepk");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("t94-poedok02413");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("309282opfkwe9r224]]k");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("4-03-0kopf3g-043itgp4igf-e0f[spf");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setTransmissionBoxTypes("+_+_!#_#$#+|#$}%4");
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void updateCar_invalid_engine_capacity() throws Exception {

        String exceptedMassage = "Engine capacity must be positive number or 0.";
        String fieldName = "engineCapacity";

        carDTO.setEngineCapacity(-0.1);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setEngineCapacity(-1.0);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setEngineCapacity(-424.35);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setEngineCapacity(-6440699.53545456);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Engine capacity must be less then 15,0.";

        carDTO.setEngineCapacity(15.1);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setEngineCapacity(15.2);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setEngineCapacity(424.35);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setEngineCapacity(6440699.53545456);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void updateCar_invalid_full_description() throws Exception {

        String exceptedMassage = "Full description must be less then 5000.";
        String fieldName = "fullDescription";

        carDTO.setFullDescription(generateRandomString(5001));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setFullDescription(generateRandomString(5002));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setFullDescription(generateRandomString(20_111));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "The full description should not contain anything but a word," +
                " numbers and punctuation marks, also cannot start with number or punctuation mark.";

        carDTO.setFullDescription("!" + generateRandomString(500));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setFullDescription("23" + generateRandomString(500));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setFullDescription("5" + generateRandomString(500));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setFullDescription("+" + generateRandomString(500));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setFullDescription(")" + generateRandomString(500));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void updateCar_invalid_short_description() throws Exception {

        String exceptedMassage = "Short description must be less then 1000.";
        String fieldName = "shortDescription";

        carDTO.setShortDescription(generateRandomString(1001));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setShortDescription(generateRandomString(1002));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setShortDescription(generateRandomString(5_029));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "The short description should not contain anything but a word," +
                " numbers and punctuation marks, also cannot start with number or punctuation mark.";

        carDTO.setShortDescription("!" + generateRandomString(500));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setShortDescription("23" + generateRandomString(500));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setShortDescription("5" + generateRandomString(500));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setShortDescription("+" + generateRandomString(500));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setShortDescription(")" + generateRandomString(500));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    void updateCar_invalid_image_file_name() throws Exception {

        String exceptedMassage = "Image file name is required and cannot be empty or null.";
        String fieldName = "imageFileId ";

        carDTO.setImageFileId(null);
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        exceptedMassage = "Image file name must be greater than 10 and less then 150.";

        carDTO.setImageFileId(generateRandomString(9));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setImageFileId(generateRandomString(8));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setImageFileId(generateRandomString(151));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setImageFileId(generateRandomString(151));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setImageFileId(generateRandomString(3));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setImageFileId(generateRandomString(374));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);

        carDTO.setImageFileId(generateRandomString(5_029));
        checkUpdateInvalidFieldCar(carDTO, fieldName, exceptedMassage);
    }

    @Test
    public void getAllCars() {

        checkGetAllCars(carDTOs);
        checkGetAllCars(generateRandomCarDTOList());
        checkGetAllCars(generateRandomCarDTOList());
        checkGetAllCars(generateRandomCarDTOList());
        checkGetAllCars(generateRandomCarDTOList());
    }

    @Test
    void getCar() {

        checkGetCar(id, car);
        checkGetCar(1L, generateRandomCar());
        checkGetCar(-1L, generateRandomCar());
        checkGetCar(2L, generateRandomCar());
        checkGetCar(-2L, generateRandomCar());
        checkGetCar(0L, generateRandomCar());
        checkGetCar(MAX_VALUE, generateRandomCar());
        checkGetCar(MAX_VALUE - 1, generateRandomCar());
        checkGetCar(MIN_VALUE, generateRandomCar());
        checkGetCar(MIN_VALUE + 1, generateRandomCar());
        checkGetCar(22_525L, generateRandomCar());
        checkGetCar(192L, generateRandomCar());
        checkGetCar(99L, generateRandomCar());
        checkGetCar(4_533L, generateRandomCar());
        checkGetCar(11L, generateRandomCar());
        checkGetCar(5L, generateRandomCar());
        checkGetCar(7_877L, generateRandomCar());
        checkGetCar(id, generateRandomCar());
        checkGetCar(id, generateRandomCar());
        checkGetCar(id, generateRandomCar());
        checkGetCar(id, generateRandomCar());
        checkGetCar(id, generateRandomCar());
        checkGetCar(id, generateRandomCar());
        checkGetCar(id, generateRandomCar());
    }

    @Test
    void getCar_with_valid_id_not_found_car() {

        Exception exception = new NoSuchCarException(format("Cannot find car with id %d", id));
        checkErrorGetCar(id, exception, format("Cannot find car with id %d", id), NOT_FOUND);
    }

    @Test
    void getCar_with_null_id() {

        Exception exception = new IllegalArgumentException("Id cannot be null");
        checkErrorGetCar(id, exception, "Id cannot be null", BAD_REQUEST);
    }

    @Test
    void removeCar() {

        String fileName = "FileName";
        MockMultipartFile file = new MockMultipartFile(
                "imageId", fileName + ".jpeg",
                String.valueOf(IMAGE_JPEG),
                "123".getBytes());

        checkRemoveCar(1L, fileName, file);
        checkRemoveCar(-1L, fileName, file);
        checkRemoveCar(2L, fileName, file);
        checkRemoveCar(-2L, fileName, file);
        checkRemoveCar(0L, fileName, file);
        checkRemoveCar(MAX_VALUE, fileName, file);
        checkRemoveCar(MAX_VALUE - 1, fileName, file);
        checkRemoveCar(MIN_VALUE, fileName, file);
        checkRemoveCar(MIN_VALUE + 1, fileName, file);
        checkRemoveCar(22_525L, fileName, file);
        checkRemoveCar(192L, fileName, file);
        checkRemoveCar(99L, fileName, file);
        checkRemoveCar(4_533L, fileName, file);
        checkRemoveCar(11L, fileName, file);
        checkRemoveCar(5L, fileName, file);
        checkRemoveCar(7_877L, fileName, file);
        checkRemoveCar(id, fileName, file);
    }

    @Test
    void removeCar_default_image() throws Exception {
//
//        String fileName = "default-car-image.png";
//
//        S3Object s3Object = s3client.getObject(bucketName, fileName);
//        assertEquals(fileName, s3Object.getKey());
//
//        mockMvc.perform(delete(format(getDeleteCarUri(id)))
//                        .param("imageId", fileName)
//                        .header(AUTHORIZATION, token)).
//                andExpect(status().isOk());
//
//        s3Object = s3client.getObject(bucketName, fileName);
//        assertEquals(fileName, s3Object.getKey());
//
//        mockMvc.perform(delete(format(getDeleteCarUri(id)))).
//                andExpect(status().isForbidden());
//
//        verify(carService, times(1)).deleteById(id);
    }

    @Test
    void removeCar_with_valid_id_not_found_car() {

        Exception serviceException = new NoSuchCarException(format("Cannot find car with id %d", id));
        checkErrorRemoveCar(id, "", serviceException, format("Cannot find car with id %d", id), NOT_FOUND);
    }

    @Test
    void removeCar_with_null_id() {

        Exception serviceException = new IllegalArgumentException("Id cannot be null");
        checkErrorRemoveCar(id, "", serviceException, "Id cannot be null", BAD_REQUEST);
    }

    @SneakyThrows
    private static void checkGetAllCars(List<CarDTO> carDTOs) {

        List<Car> serviceCarList = carDTOs.stream().map(CarDTO::toEntity).collect(Collectors.toList());

        when(carService.findAll()).thenReturn(serviceCarList);
        String exceptedJSON = objectMapper.writeValueAsString(carDTOs);

        mockMvc.perform(get(getAllCarURI())).
                andExpect(status().isOk()).
                andExpect(content().string(containsString(exceptedJSON))).
                andReturn();

        verify(carService, times(1)).findAll();
        clearInvocations(carService);
    }

    @SneakyThrows
    private static void checkGetCar(Long id, Car exceptedCar) {

        exceptedCar.setId(id);
        when(carService.findById(id)).thenReturn(exceptedCar);
        String exceptedJSON = objectMapper.writeValueAsString(toDTO(exceptedCar));

        mockMvc.perform(get(getGetCarUri(id))).
                andExpect(status().isOk()).
                andExpect(content().string(containsString(exceptedJSON))).
                andReturn();

        verify(carService, times(1)).findById(id);
        clearInvocations(carService);
    }

    @SneakyThrows
    private static void checkErrorGetCar(Long id, Exception serviceException, String exceptedErrorMassage,
                                         HttpStatus exceptedHttpStatus) {

        when(carService.findById(id)).thenThrow(serviceException);

        mockMvc.perform(get(getGetCarUri(id))).
                andExpect(status().is(exceptedHttpStatus.value())).
                andExpect(content().string(containsString(exceptedErrorMassage))).
                andReturn();

        verify(carService, times(1)).findById(id);
        clearInvocations(carService);
    }

    @SneakyThrows
    private static void checkAddCar(CarDTO carDTO) {

        Car returnServiceCar = carDTO.toEntity();

        when(carService.save(any(Car.class))).thenReturn(returnServiceCar);
        String requestJSON = objectMapper.writeValueAsString(carDTO);
        String responseJSON = objectMapper.writeValueAsString(carDTO);

        mockMvc.perform(post(getAddCarUri()).
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        characterEncoding("utf-8").
                        accept(APPLICATION_JSON).
                        header(AUTHORIZATION, token)
                ).
                andExpect(status().isCreated()).
                andExpect(content().json(responseJSON)).
                andReturn();

        mockMvc.perform(post(getAddCarUri()).
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        characterEncoding("utf-8").
                        accept(APPLICATION_JSON)
                ).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(1)).save(any(Car.class));
        clearInvocations(carService);
    }

    @SneakyThrows
    private static void checkUpdateCar(CarDTO carDTO) {

        Car serviceCar = carDTO.toEntity();
        Car returnServiceCar = carDTO.toEntity();

        when(carService.update(any(Car.class))).thenReturn(serviceCar);
        String requestJSON = objectMapper.writeValueAsString(carDTO);
        String responseJSON = objectMapper.writeValueAsString(carDTO);

        mockMvc.perform(put(getUpdateCarUri()).
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        characterEncoding("utf-8").
                        accept(APPLICATION_JSON).
                        header(AUTHORIZATION, token)
                ).
                andExpect(status().isOk()).
                andExpect(content().json(responseJSON)).
                andReturn();

        mockMvc.perform(put(getUpdateCarUri()).
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        characterEncoding("utf-8").
                        accept(APPLICATION_JSON)
                ).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(1)).update(any(Car.class));
        clearInvocations(carService);
    }

    @SneakyThrows
    private static void checkRemoveCar(Long deleteCarID, String deleteCarImageId, MockMultipartFile image) {

        amazonClient.uploadFileTos3bucket(deleteCarImageId, image);
        doNothing().when(carService).deleteById(deleteCarID);
//
        S3Object s3Object = s3client.getObject(CarControllerTest.bucketName, deleteCarImageId);

        assertEquals(deleteCarImageId, s3Object.getKey());

        mockMvc.perform(delete(format(getDeleteCarUri(deleteCarID)))
                        .param("imageId", deleteCarImageId)
                        .header(AUTHORIZATION, token)).
                andExpect(status().isOk());

        assertThatThrownBy(() -> s3client.getObject(CarControllerTest.bucketName, deleteCarImageId)).
                isInstanceOf(AmazonS3Exception.class);

        mockMvc.perform(delete(format(getDeleteCarUri(deleteCarID)))).
                andExpect(status().isForbidden());

        verify(carService, times(1)).deleteById(deleteCarID);
        clearInvocations(carService);
    }

    @SneakyThrows
    private static void checkErrorRemoveCar(Long id, String imageId, Exception serviceException, String exceptedErrorMassage,
                                            HttpStatus exceptedHttpStatus) {

        doThrow(serviceException).
                when(carService).
                deleteById(id);

        mockMvc.perform(delete(format(getDeleteCarUri(id)))
                        .header(AUTHORIZATION, token)
                        .param("imageId", imageId)).
                andExpect(status().is(exceptedHttpStatus.value())).
                andExpect(content().string(containsString(exceptedErrorMassage))).
                andReturn();

        mockMvc.perform(delete(format(getDeleteCarUri(id)))).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(1)).deleteById(id);
        clearInvocations(carService);
    }

    @SneakyThrows
    public static void checkErrorUpdateCar(CarDTO updateCar, Exception serviceException, String exceptedErrorMassage,
                                           HttpStatus exceptedHttpStatus) {

        when(carService.update(any(Car.class))).thenThrow(serviceException);

        String requestJSON = objectMapper.writeValueAsString(updateCar);

        mockMvc.perform(put(getUpdateCarUri()).
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        characterEncoding("utf-8").
                        accept(APPLICATION_JSON).
                        header(AUTHORIZATION, token)
                ).
                andExpect(status().is(exceptedHttpStatus.value())).
                andExpect(jsonPath("$.error_message").
                        value(exceptedErrorMassage)).
                andReturn();

        mockMvc.perform(put(getUpdateCarUri()).
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        characterEncoding("utf-8").
                        accept(APPLICATION_JSON)
                ).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(1)).update(any(Car.class));
        clearInvocations(carService);
    }

    private static void checkAddInvalidFieldCar(CarDTO carDTO, String jsonInvalidFieldName,
                                                String exceptedErrorMassage) throws Exception {

        String requestJSON = objectMapper.writeValueAsString(carDTO);

        mockMvc.perform(post(getAddCarUri()).
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        accept(APPLICATION_JSON).
                        header(AUTHORIZATION, token)
                ).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath(format("%s", jsonInvalidFieldName)).
                        value(exceptedErrorMassage)).
                andReturn();

        mockMvc.perform(post(getAddCarUri()).
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        accept(APPLICATION_JSON)
                ).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(0)).save(any(Car.class));
    }

    private static void checkUpdateInvalidFieldCar(CarDTO carDTO, String jsonInvalidFieldName,
                                                   String exceptedErrorMassage) throws Exception {

        String requestJSON = objectMapper.writeValueAsString(carDTO);

        mockMvc.perform(put(getUpdateCarUri()).
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        accept(APPLICATION_JSON).
                        header(AUTHORIZATION, token)
                ).
                andExpect(status().isBadRequest()).
                andExpect(jsonPath(format("%s", jsonInvalidFieldName)).
                        value(exceptedErrorMassage)).
                andReturn();

        mockMvc.perform(post(getUpdateCarUri()).
                        contentType(APPLICATION_JSON).
                        content(requestJSON).
                        accept(APPLICATION_JSON)
                ).
                andExpect(status().isForbidden()).
                andReturn();

        verify(carService, times(0)).save(any(Car.class));
    }

    @Autowired
    public void setS3client(AmazonS3 s3client) {
        CarControllerTest.s3client = s3client;
    }

    @Autowired
    public void setAmazonClient(AmazonClient amazonClient) {
        CarControllerTest.amazonClient = amazonClient;
    }

    @Autowired
    public void setAuthService(AuthService authService) {
        authTestUtils = new AuthTestUtils(authService);
    }
}