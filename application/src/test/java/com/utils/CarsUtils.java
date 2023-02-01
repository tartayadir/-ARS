package com.utils;

import com.implemica.model.car.dto.CarDTO;
import com.implemica.model.car.entity.Car;
import com.implemica.model.car.enums.CarBodyType;
import com.implemica.model.car.enums.CarBrand;
import com.implemica.model.car.enums.TransmissionBoxType;
import org.fluttercode.datafactory.impl.DataFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.utils.EnumUtils.getRandomEnumValue;

public class CarsUtils {

    private static final DataFactory dataFactory = new DataFactory();

    private static final Random random = new Random();

    public static Car generateRandomCar() {

        return Car.builder().
                id((long) dataFactory.getNumberBetween(1, 100)).
                brand(getRandomEnumValue(CarBrand.class)).
                model(dataFactory.getFirstName()).
                carBodyTypes(getRandomEnumValue(CarBodyType.class)).
                year((short) dataFactory.getNumberBetween(1_921, 2_020)).
                transmissionBoxTypes(getRandomEnumValue(TransmissionBoxType.class)).
                engineCapacity(dataFactory.getNumberBetween(1, 11)).
                shortDescription(dataFactory.getRandomText(10, 200)).
                fullDescription(dataFactory.getRandomText(10, 1_000)).
                additionalOptions(List.of(dataFactory.getRandomWord(2, 10), dataFactory.getRandomWord(2, 10),
                        dataFactory.getRandomWord(2, 10))).
                imageFileName(dataFactory.getRandomWord(11, 20)).
                build();
    }

    public static CarDTO generateRandomCarDto() {

        return CarDTO.builder().
                id((long) dataFactory.getNumberBetween(1, 100)).
                brand(getRandomEnumValue(CarBrand.class).toString()).
                model(dataFactory.getFirstName()).
                carBodyTypes(getRandomEnumValue(CarBodyType.class).toString()).
                year((short) dataFactory.getNumberBetween(1_921, 2_020)).
                transmissionBoxTypes(getRandomEnumValue(TransmissionBoxType.class).toString()).
                engineCapacity((double)dataFactory.getNumberBetween(1, 11)).
                shortDescription(dataFactory.getRandomText(10, 200)).
                fullDescription(dataFactory.getRandomText(10, 1_000)).
                additionalOptions(List.of(dataFactory.getRandomWord(2, 10), dataFactory.getRandomWord(2, 10),
                        dataFactory.getRandomWord(2, 10))).
                imageFileId(dataFactory.getRandomWord(11, 20)).
                build();
    }

    public static List<Car> generateRandomCarList() {

        List<Car> list = new ArrayList<>();
        int listSize = dataFactory.getNumberBetween(3, 10);

        for (int i = 0; i < listSize; i++) {

            list.add(generateRandomCar());
        }

        return list;
    }

    public static List<CarDTO> generateRandomCarDTOList() {

        List<CarDTO> list = new ArrayList<>();
        int listSize = dataFactory.getNumberBetween(3, 10);

        for (int i = 0; i < listSize; i++) {

            list.add(generateRandomCarDto());
        }

        return list;
    }
}
