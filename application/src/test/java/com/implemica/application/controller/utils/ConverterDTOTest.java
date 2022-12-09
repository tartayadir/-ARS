package com.implemica.application.controller.utils;

import com.implemica.controller.utils.ConverterDTO;
import com.implemica.model.car.dto.CarDTO;
import com.implemica.model.car.entity.Car;
import com.implemica.model.car.entity.CarBodyTypes;
import com.implemica.model.car.entity.CarBrands;
import com.implemica.model.car.entity.TransmissionBoxTypes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static utils.spring.FileUtils.*;

@Slf4j
class ConverterDTOTest {

    private static Car car;

    private static CarDTO carDTO;

    private static final String fileName = "testFile";

    @BeforeEach
    void setUp() {

        short year = 2012;
        car = Car.builder().
                id(10L).
                brand(CarBrands.ALFA).
                model("Model 1").
                carBodyTypes(CarBodyTypes.SPORTS_CAR).
                year(year).
                transmissionBoxTypes(TransmissionBoxTypes.AUTOMATIC).
                engineCapacity(5.4).
                shortDescription("Short description 1").
                fullDescription("Full description 1").
                additionalOptions(List.of("Option 1", "Option 2", "option 3")).
                imageFileName("Image file 1").
                build();

        year = 2019;
        carDTO = CarDTO.builder().
                id(19L).
                brand("AUDI").
                model("Model 2").
                carBodyTypes("COUPE").
                year(year).
                transmissionBoxTypes("ROBOTIC").
                engineCapacity(1.9).
                shortDescription("Short description 2").
                fullDescription("Full description 2").
                additionalOptions(List.of("Option 4", "Option 5", "option 6")).
                imageFileName("Image file 2").
                build();
    }

    @Test
    void carEntityToDTO() {

        CarDTO carDTOAfterConvert = ConverterDTO.carEntityToDTO(car);
        assertEqualsCarAndCarDTO(car, carDTOAfterConvert);
    }

    @Test
    void dtoToCarEntity() {

        Car carAfterConvert = ConverterDTO.dtoToCarEntity(carDTO);
        assertEqualsCarAndCarDTO(carAfterConvert, carDTO);
    }

    @Test
    void convertMultiPartToFile() {

        byte[] bytes = new byte[]{23, 24, 3};
        MultipartFile multipartFile = createMultipartFile(fileName, bytes);
        File file = createFile(fileName, bytes);

        assertEqualsFileAndMultiPartFile(file, multipartFile);

        bytes = new byte[]{42, 127, 32};
        multipartFile = createMultipartFile(fileName, bytes);
        file = createFile(fileName, bytes);

        assertEqualsFileAndMultiPartFile(file, multipartFile);
    }

    private static void assertEqualsCarAndCarDTO(Car car, CarDTO carDTO) {

        Long carId = car.getId();
        Long carDtoId = carDTO.getId();
        assertEquals(carId, carDtoId);

        CarBrands carBrand = car.getBrand();
        CarBrands carDtoBrand = CarBrands.valueOf(carDTO.getBrand());
        assertEquals(carBrand, carDtoBrand);

        String carBrandAsString = car.getBrand().getStringValue();
        String carDtoBrandAsString = carDTO.getBrand();
        assertEquals(carBrandAsString, carDtoBrandAsString);

        String carModel = car.getModel();
        String carDtoModel = carDTO.getModel();
        assertEquals(carModel, carDtoModel);

        short carYear = car.getYear();
        short carDtoYear = carDTO.getYear();
        assertEquals(carYear, carDtoYear);

        TransmissionBoxTypes carTransmission = car.getTransmissionBoxTypes();
        TransmissionBoxTypes carDtoTransmission = TransmissionBoxTypes.valueOf(carDTO.getTransmissionBoxTypes());
        assertEquals(carTransmission, carDtoTransmission);

        String carTransmissionAsString = car.getTransmissionBoxTypes().getStringValue();
        String carDtoTransmissionAsString = carDTO.getTransmissionBoxTypes();
        assertEquals(carTransmissionAsString, carDtoTransmissionAsString);

        double carEngineCapacity = car.getEngineCapacity();
        double carDtoEngineCapacity = carDTO.getEngineCapacity();
        assertEquals(carEngineCapacity, carDtoEngineCapacity);

        String carShortDescription = car.getShortDescription();
        String carDtoShortDescription = carDTO.getShortDescription();
        assertEquals(carShortDescription, carDtoShortDescription);

        String carFullDescription = car.getFullDescription();
        String carDTOFullDescription = carDTO.getFullDescription();
        assertEquals(carFullDescription, carDTOFullDescription);

        CarBodyTypes carCarBodyTypes = car.getCarBodyTypes();
        CarBodyTypes carDtoBodyType = CarBodyTypes.valueOf(carDTO.getCarBodyTypes());
        assertEquals(carCarBodyTypes, carDtoBodyType);

        String carCarBodyTypesAsString = car.getCarBodyTypes().getStringValue();
        String carDtoBodyTypeAsString = carDTO.getCarBodyTypes();
        assertEquals(carDtoBodyTypeAsString, carCarBodyTypesAsString);

        List<String>  carAdditionalOptions = car.getAdditionalOptions();
        List<String>  carDtoAdditionalOptions = carDTO.getAdditionalOptions();
        assertEquals(carAdditionalOptions, carDtoAdditionalOptions);

        String carImageNameFile = car.getImageFileName();
        String carDtoImageNameFile = carDTO.getImageFileName();
        assertEquals(carImageNameFile, carDtoImageNameFile);
    }

    private static void assertEqualsFileAndMultiPartFile(File file, MultipartFile multipartFile) {

        try {

            File multipartFileFile = ConverterDTO.convertMultiPartToFile(multipartFile);

            byte[] actualBytes = FileUtils.readFileToByteArray(file);
            byte[] exceptedBytes = FileUtils.readFileToByteArray(multipartFileFile);
            assertArrayEquals(exceptedBytes, actualBytes);

            String actualName = file.getName();
            String exceptedName = multipartFileFile.getName();
            assertEquals(exceptedName, actualName);
        } catch (IOException ex) {

            log.error(ex.getMessage(), (Object) ex.getStackTrace());
            fail();
        }

    }
}