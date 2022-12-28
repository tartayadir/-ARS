package com.implemica.controller.utils;

import com.implemica.model.car.dto.CarDTO;
import com.implemica.model.car.entity.Car;
import com.implemica.model.car.entity.CarBodyTypes;
import com.implemica.model.car.entity.CarBrands;
import com.implemica.model.car.entity.TransmissionBoxTypes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class ConverterDTO {

    private ConverterDTO() {}

    public static CarDTO carEntityToDTO(Car car) {

        CarDTO carDTO = CarDTO.builder().build();

        carDTO.setId(car.getId());
        carDTO.setBrand(car.getBrand().toString());
        carDTO.setModel(car.getModel());
        carDTO.setCarBodyTypes(car.getCarBodyTypes().toString());
        carDTO.setYear(car.getYear());
        carDTO.setTransmissionBoxTypes(car.getTransmissionBoxTypes().getStringValue());
        carDTO.setEngineCapacity(car.getEngineCapacity());
        carDTO.setShortDescription(car.getShortDescription());
        carDTO.setFullDescription(car.getFullDescription());
        carDTO.setAdditionalOptions(car.getAdditionalOptions());
        carDTO.setImageFileId(car.getImageFileName());

        return carDTO;
    }

    public static Car dtoToCarEntity(CarDTO carDTO) {

        Car car = new Car();
        car.setId(carDTO.getId());
        car.setBrand(CarBrands.valueOf(carDTO.getBrand()));
        car.setModel(carDTO.getModel());
        car.setCarBodyTypes(CarBodyTypes.valueOf(carDTO.getCarBodyTypes()));
        car.setYear(carDTO.getYear());
        car.setTransmissionBoxTypes(TransmissionBoxTypes.valueOf(carDTO.getTransmissionBoxTypes()));
        car.setEngineCapacity(carDTO.getEngineCapacity());
        String shortDescription = carDTO.getShortDescription();
        car.setShortDescription(shortDescription == null ? "" : shortDescription);
        String fullDescription = carDTO.getFullDescription();
        car.setFullDescription(fullDescription == null ? "" : fullDescription);
        car.setAdditionalOptions(carDTO.getAdditionalOptions());
        car.setImageFileName(carDTO.getImageFileId());

        return car;
    }

    public static File convertMultiPartToFile(MultipartFile file) {

        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

        try {

            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            log.error(e.getMessage(), (Object) e.getStackTrace());
        }

        return convFile;
    }

}
