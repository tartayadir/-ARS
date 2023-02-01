package com.implemica.application.controller.controllers;

import io.swagger.client.ApiException;
import io.swagger.client.api.CarApiApi;
import io.swagger.client.model.CarDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.utils.swagger.AuthUtils.getAuthorizedApiClient;
import static com.utils.swagger.CarDtoUtils.generateRandomCar;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarControllerTestWithSwagger {

    private static final String DEFAULT_IMAGE_NAME = "default-car-image";

    private static CarApiApi authorizedCarApi;

    private static CarApiApi notAuthorizedCarApi;

    @BeforeAll
    static void beforeAll() {

        authorizedCarApi = new CarApiApi(getAuthorizedApiClient());
        notAuthorizedCarApi = new CarApiApi();
    }

    @Test
    public void checkCarAllActions() {

        checkCar(generateRandomCar(), generateRandomCar());
        checkCar(generateRandomCar(), generateRandomCar());
        checkCar(generateRandomCar(), generateRandomCar());
        checkCar(generateRandomCar(), generateRandomCar());
        checkCar(generateRandomCar(), generateRandomCar());
    }

    private static void checkCar(CarDTO addCarDto, CarDTO updateCarDto) {

        addCarDto = addCar(addCarDto);
        Long carId = addCarDto.getId();

        editCar(carId, updateCarDto);

        deleteCar(carId);
    }

    @SneakyThrows
    private static CarDTO addCar(CarDTO carDTO) {

        CarDTO addCar = authorizedCarApi.addCarUsingPOST(carDTO);
        Long carId = addCar.getId();

        carDTO.setId(carId);
        assertEquals(carDTO, addCar);

        assertThatThrownBy(() -> notAuthorizedCarApi.addCarUsingPOSTWithHttpInfo(carDTO)).
                isInstanceOf(ApiException.class);

        checkGetCar(carId, carDTO);

        return addCar;
    }

    @SneakyThrows
    private static void editCar(Long carId, CarDTO carDTO) {

        carDTO.setId(carId);

        CarDTO updateCarDto = authorizedCarApi.updateCarUsingPUT(carDTO);
        assertEquals(carDTO, updateCarDto);

        assertThatThrownBy(() -> notAuthorizedCarApi.updateCarUsingPUTWithHttpInfo(carDTO)).
                isInstanceOf(ApiException.class);

        checkGetCar(carId, carDTO);

    }

    @SneakyThrows
    private static void deleteCar(Long carID){

        authorizedCarApi.removeCarUsingDELETE(carID, DEFAULT_IMAGE_NAME);

        assertThatThrownBy(() -> authorizedCarApi.getCarUsingGET(carID)).
                isInstanceOf(ApiException.class);
    }

    @SneakyThrows
    private static void checkGetCar(Long carId, CarDTO carDTO){

        CarDTO getCar = authorizedCarApi.getCarUsingGET(carId);
        assertEquals(carDTO, getCar);

        getCar = notAuthorizedCarApi.getCarUsingGET(carId);
        assertEquals(carDTO, getCar);
    }
}
