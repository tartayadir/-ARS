package com.implemica.controller.service.car.service;

import com.implemica.controller.exceptions.NoSuchCarException;
import com.implemica.model.car.entity.Car;

import java.util.List;

public interface CarService {

    List<Car> findAll();

    Car findById(Long id) throws NoSuchCarException;

    Car save(Car car);

    void deleteById(Long id) throws NoSuchCarException;

    Car update(Car car) throws NoSuchCarException;
}
