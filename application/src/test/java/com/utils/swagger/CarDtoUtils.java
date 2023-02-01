package com.utils.swagger;

import io.swagger.client.model.CarDTO;
import org.fluttercode.datafactory.impl.DataFactory;

import java.util.List;

import static com.utils.EnumUtils.getRandomEnumValue;

public class CarDtoUtils {

    private static final DataFactory dataFactory = new DataFactory();

    public static CarDTO generateRandomCar() {

        return CarDTO.builder().
                id((long) dataFactory.getNumberBetween(1, 100)).
                brand(getRandomEnumValue(CarDTO.BrandEnum.class)).
                model(dataFactory.getFirstName()).
                carBodyTypes(getRandomEnumValue(CarDTO.CarBodyTypesEnum.class)).
                year(dataFactory.getNumberBetween(1_921, 2_020)).
                transmissionBoxTypes(getRandomEnumValue(CarDTO.TransmissionBoxTypesEnum.class)).
                engineCapacity((double)dataFactory.getNumberBetween(1, 11)).
                shortDescription(dataFactory.getRandomText(10, 200)).
                fullDescription(dataFactory.getRandomText(10, 1_000)).
                additionalOptions(List.of(dataFactory.getRandomWord(2, 10), dataFactory.getRandomWord(2, 10), dataFactory.getRandomWord(2, 10))).
                imageFileId("default-car-image").
                build();
    }
}
