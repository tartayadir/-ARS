package com.utils;

import com.implemica.model.car.entity.Car;
import com.implemica.model.car.entity.CarBodyTypes;
import com.implemica.model.car.entity.CarBrands;
import com.implemica.model.car.entity.TransmissionBoxTypes;
import org.fluttercode.datafactory.impl.DataFactory;

import java.util.List;

public class CarsUtils {

    private static final DataFactory dataFactory = new DataFactory();

    public static Car generateRandomCar() {

        return Car.builder().
                id((long) dataFactory.getNumberBetween(1, 100)).
                brand(CarBrands.getRandomCarBrand()).
                model(dataFactory.getFirstName()).
                carBodyTypes(CarBodyTypes.getRandomCarBodyType()).
                year((short) dataFactory.getNumberBetween(1_921, 2_020)).
                transmissionBoxTypes(TransmissionBoxTypes.getRandomTransmissionBoxTypes()).
                engineCapacity(dataFactory.getNumberBetween(1, 11)).
                shortDescription(dataFactory.getRandomText(10, 200)).
                fullDescription(dataFactory.getRandomText(10, 1_000)).
                additionalOptions(List.of(dataFactory.getRandomWord(2, 10), dataFactory.getRandomWord(2, 10), dataFactory.getRandomWord(2, 10))).
                imageFileName(dataFactory.getName()).
                build();
    }
}
