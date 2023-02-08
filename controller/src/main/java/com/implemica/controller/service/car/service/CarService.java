package com.implemica.controller.service.car.service;

import com.implemica.controller.exceptions.NoSuchCarException;
import com.implemica.model.car.entity.Car;

import java.util.List;

/**
 * Provides GRUD operations for {@link Car}
 */
public interface CarService {

    /**
     * Returns all cars in the database
     *
     * @return cars list
     */
    List<Car> findAll();

    /**
     * Returns car with provided id from database
     *
     * @param id car id in database
     * @return car
     * @throws NoSuchCarException if car with provide id is not present in database
     */
    Car findById(Long id) throws NoSuchCarException;

    /**
     * Add car to database
     *
     * @param car car that will be added
     * @return add car
     */
    Car save(Car car);

    /**
     * Deletes car with provided id in database
     *
     * @param id car id in database
     * @throws NoSuchCarException if car with provide id is not present in database
     */
    void deleteById(Long id) throws NoSuchCarException;

    /**
     * Find car in database, updates fields and saves changed car in database
     *
     * @param car car that will be updated
     * @return update car
     * @throws NoSuchCarException if car with provide update car id is not present in database
     */
    Car update(Car car) throws NoSuchCarException;
}
