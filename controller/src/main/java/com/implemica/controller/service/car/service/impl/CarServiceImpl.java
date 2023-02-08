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
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.transaction.annotation.Isolation.SERIALIZABLE;

/**
 * Implements {@link CarService} interface and methods that is
 * CRUD operations for {@link Car} entity with checks on validation
 * cars id and database availability. It has a SERIALIZABLE transactional isolation level
 * and caching methods results for optimize and reduce requests to database.
 *
 */
@Service
@AllArgsConstructor
@Transactional(isolation = SERIALIZABLE)
public class CarServiceImpl implements CarService {

    /**
     * Car DAO
     */
    private CarRepository carRepository;

    /**
     * Returns all cars in the database using car DAO. Result are saved in cache.
     *
     * @return cars list
     */
    @Override
    @Cacheable(cacheNames = "cars")
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    /**
     * Returns car with provided id from database using car DAO,
     * pre-check availability in the database and correctness car id.
     * Return car is saved in cache and id is used like key.
     *
     * @param id car id in database
     * @return car
     * @throws NoSuchCarException if car with provide id is not present in database
     */
    @Override
    @Cacheable(cacheNames = "car", key = "#id")
    public Car findById(Long id) throws NoSuchCarException {

        return carRepository.findById(
                Optional.ofNullable(id).orElseThrow(() ->
                        new IllegalArgumentException("Id cannot be null"))).orElseThrow(() ->
                        new NoSuchCarException(format("Cannot find car with id %d", id)));
    }

    /**
     * Add car to database using car DAO. And clean cache for
     * {@link CarServiceImpl#findAll()}, because cars list was changed.
     *
     * @param car car that will be added
     * @return add car
     */
    @Override
    @CacheEvict(cacheNames = "cars", allEntries=true)
    public Car save(Car car) {
        return carRepository.save(car);
    }

    /**
     * Deletes car with provided id in database, pre-check availability in the database and
     * correctness car id. And clean cache for {@link CarServiceImpl#findAll()} and
     * {@link CarServiceImpl#findById(Long)}, because cars list was changed and car with its id was deleted.
     *
     * @param id car id in database
     * @throws NoSuchCarException if car with provide id is not present in database
     * @throws IllegalArgumentException if provided id is null
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = "cars", allEntries=true), @CacheEvict(cacheNames = "car", key = "#id")})
    public void deleteById(Long id) throws NoSuchCarException{

        carRepository.delete(carRepository.findById(
                Optional.ofNullable(id).orElseThrow(() ->
                        new IllegalArgumentException("Id cannot be null"))).orElseThrow(() ->
                        new NoSuchCarException(format("Cannot find car with id %d", id))));
    }

    /**
     * Find car in database, updates fields and saves changed car in database,
     * pre-check availability in the database and correctness car id. And clean
     * cache for {@link CarServiceImpl#findAll()} and {@link CarServiceImpl#findById(Long)},
     * because cars list and car with its id was changed.
     *
     * @param car car that will be updated
     * @return update car
     * @throws NoSuchCarException if car with provide update car id is not present in database
     */
    @Override
    @Caching(evict = { @CacheEvict(cacheNames = "cars", allEntries=true), @CacheEvict(cacheNames = "car", key = "#car.id")})
    public Car update(Car car) throws NoSuchCarException{

        Long carId = car.getId();

        Car carFromDB = carRepository.findById(
                Optional.ofNullable(carId).orElseThrow(() ->
                        new IllegalArgumentException("Id cannot be null"))).orElseThrow(() ->
                        new NoSuchCarException(format("Cannot find and update car with id %d", carId)));

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
