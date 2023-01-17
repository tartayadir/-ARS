package com.implemica.controller.service.car.service.impl;

import com.implemica.controller.exceptions.NoSuchCarException;
import com.implemica.controller.service.car.service.CarService;
import com.implemica.model.car.entity.Car;
import com.implemica.model.car.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

@Service
@AllArgsConstructor
@Transactional(isolation = SERIALIZABLE)
public class CarServiceImpl implements CarService {

    private CarRepository carRepository;

    @Override
    @Cacheable(cacheNames = "cars")
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    @Cacheable(cacheNames = "car", key = "#id")
    public Car findById(Long id) throws NoSuchCarException {

        if (id == null){
            throw new IllegalArgumentException("Id cannot be null");
        }

        return carRepository.findById(id).orElseThrow(
                () -> new NoSuchCarException(format("Cannot find car with id %d", id)));
    }

    @Override
    @CacheEvict(cacheNames = "cars", allEntries=true)
    public Car save(Car car) {
        return carRepository.save(car);
    }

    @Override
    @Caching(evict = { @CacheEvict(cacheNames = "cars", allEntries=true), @CacheEvict(cacheNames = "car", key = "#id")})
    public void deleteById(Long id) throws NoSuchCarException{

        if (id == null){
            throw new IllegalArgumentException("Id cannot be null");
        }

        carRepository.delete(carRepository.findById(id).orElseThrow(
                () -> new NoSuchCarException(format("Cannot find car with id %d", id))));
    }

    @Override
    @Caching(evict = { @CacheEvict(cacheNames = "cars", allEntries=true), @CacheEvict(cacheNames = "car", key = "#car.id")})
    public Car update(Car car) throws NoSuchCarException{

        if (car.getId() == null){
            throw new IllegalArgumentException("Id cannot be null");
        }

        Car carFromDB = carRepository.findById(car.getId()).orElseThrow(() ->
                new NoSuchCarException(format("Cannot find and update car with id %d", car.getId())));
        carFromDB.setBrand(car.getBrand());
        carFromDB.setModel(car.getModel());
        carFromDB.setYear(car.getYear());
        carFromDB.setEngineCapacity(car.getEngineCapacity());
        carFromDB.setCarBodyTypes(car.getCarBodyTypes());
        carFromDB.setTransmissionBoxTypes(car.getTransmissionBoxTypes());
        carFromDB.setFullDescription(car.getFullDescription());
        carFromDB.setShortDescription(car.getShortDescription());
        carFromDB.setImageFileName(car.getImageFileName());
        carFromDB.setAdditionalOptions(car.getAdditionalOptions());

        return this.carRepository.save(carFromDB);
    }
}
