package com.utils.swagger;

import com.implemica.swagger.client.codegen.rest.model.CarDTO;
import org.fluttercode.datafactory.impl.DataFactory;

import java.util.List;

import static com.utils.EnumUtils.getRandomEnumValue;

public class CarDtoUtils {

    private static final DataFactory dataFactory = new DataFactory();

    public static CarDTO generateRandomCar() {

        CarDTO carDTO = new CarDTO();
        carDTO.setId((long) dataFactory.getNumberBetween(1, 100));
        carDTO.setBrand(getRandomEnumValue(CarDTO.BrandEnum.class));
        carDTO.setModel(dataFactory.getFirstName());
        carDTO.setCarBodyTypes(getRandomEnumValue(CarDTO.CarBodyTypesEnum.class));
        carDTO.setYear(dataFactory.getNumberBetween(1_921, 2_020));
        carDTO.setTransmissionBoxTypes(getRandomEnumValue(CarDTO.TransmissionBoxTypesEnum.class));
        carDTO.setEngineCapacity((double)dataFactory.getNumberBetween(1, 11));
        carDTO.setShortDescription(dataFactory.getRandomText(10, 200));
        carDTO.setFullDescription(dataFactory.getRandomText(10, 1_000));
        carDTO.setAdditionalOptions((List.of(dataFactory.getRandomWord(2, 10), dataFactory.getRandomWord(2, 10), dataFactory.getRandomWord(2, 10))));
        carDTO.setImageFileId("default-car-image");

        return carDTO;
    }
}