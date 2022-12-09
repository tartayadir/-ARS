package com.implemica.application.controller.service.car.service.impl;

import com.implemica.controller.exceptions.NoSuchCarException;
import com.implemica.controller.service.car.service.impl.CarServiceImpl;
import com.implemica.model.car.entity.Car;
import com.implemica.model.car.entity.CarBodyTypes;
import com.implemica.model.car.entity.CarBrands;
import com.implemica.model.car.entity.TransmissionBoxTypes;
import com.implemica.model.car.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Long.MAX_VALUE;
import static java.lang.Long.MIN_VALUE;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class CarServiceImplTest {

    @Mock
    private static CarRepository carRepository;

    private static CarServiceImpl carService;

    private static Car car;

    @BeforeEach
    public void setUp() {

        carService = new CarServiceImpl(carRepository);

        short year = 2012;
        car = Car.builder().
                brand(CarBrands.BENTLEY).
                model("AX 2").
                carBodyTypes(CarBodyTypes.SPORTS_CAR).
                year(year).
                transmissionBoxTypes(TransmissionBoxTypes.AUTOMATIC).
                engineCapacity(3.2).
                shortDescription("").
                fullDescription("").
                imageFileName("123Image").
                additionalOptions(List.of("a", "b", "c")).
                build();

        reset(carRepository);
    }

    @Test
    void update() {

        try {
            car.setId(10L);
            when(carRepository.findById(10L)).thenReturn(Optional.of(car));
            when(carRepository.save(car)).thenReturn(car);

            Car carFromService = carService.update(car);
            assertEquals(car, carFromService);

            verify(carRepository, times(1)).save(car);

            car = null;
            assertThatThrownBy( () -> carService.update(car)).
                    isInstanceOf(NullPointerException.class);
        } catch (NoSuchCarException e){

            log.error(e.getMessage(), (Object) e.getStackTrace());
            fail();
        }
    }

    @Test
    void update_not_found_car() {

        Long id = 10L;
        car.setId(id);

        when(carRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy( () -> carService.update(car)).
                isInstanceOf(NoSuchCarException.class).
                hasMessageContaining(format("Cannot find and update car with id %d", car.getId()));
    }

    @Test
    void update_id_null() {

        car.setId(null);
        assertThatThrownBy( () -> carService.update(car)).
                isInstanceOf(IllegalArgumentException.class).
                hasMessageContaining("Id cannot be null");
    }

    @Test
    void save() {

        when(carRepository.save(car)).thenReturn(car);
        Car carFromService = carService.save(car);
        assertEquals(car, carFromService);

        verify(carRepository, times(1)).save(car);
    }

    @Test
    void save_null_car() {

        car = null;
        when(carRepository.save(car)).thenThrow(IllegalArgumentException.class);
        assertThatThrownBy( () -> carService.save(car)).
                isInstanceOf(IllegalArgumentException.class);

        verify(carRepository, times(1)).save(car);
    }

    @Test
    void findAll() {

        List<Car> cars = List.of(
                Car.builder().build(),
                Car.builder().build(),
                Car.builder().build(),
                Car.builder().build()
        );

        when(carRepository.findAll()).thenReturn(cars);
        List<Car> carsFromService = carService.findAll();
        assertEquals(cars, carsFromService);

        cars = new ArrayList<>();
        when(carRepository.findAll()).thenReturn(cars);
        carsFromService = carService.findAll();
        assertEquals(cars, carsFromService);

        verify(carRepository, times(2)).findAll();
    }

    @Test
    void deleteById() {

        checkCarServiceMethodDeleteById(1L);
        checkCarServiceMethodDeleteById(-1L);
        checkCarServiceMethodDeleteById(2L);
        checkCarServiceMethodDeleteById(-2L);
        checkCarServiceMethodDeleteById(0L);
        checkCarServiceMethodDeleteById(MAX_VALUE);
        checkCarServiceMethodDeleteById(MAX_VALUE - 1);
        checkCarServiceMethodDeleteById(MIN_VALUE);
        checkCarServiceMethodDeleteById(MIN_VALUE + 1);
    }

    @Test
    void deleteById_null() {

        Long id = null;

        lenient().when(carRepository.findById(id)).thenThrow(IllegalArgumentException.class);

        lenient().doThrow(IllegalArgumentException.class).
                when(carRepository).
                delete(any(Car.class));

        assertThatThrownBy( () -> carService.deleteById(id)).
                isInstanceOf(IllegalArgumentException.class);

        verify(carRepository, times(0)).delete(any(Car.class));
    }

    @Test
    void deleteById_not_found_car() {

        Long id = 10L;
        car.setId(id);

        when(carRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy( () -> carService.deleteById(id)).
                isInstanceOf(NoSuchCarException.class).
                hasMessageContaining(format("Cannot find car with id %d", id));

        verify(carRepository, times(1)).findById(id);
    }

    @Test
    void findById() {

        checkCarServiceMethodFindById(1L);
        checkCarServiceMethodFindById(-1L);
        checkCarServiceMethodFindById(2L);
        checkCarServiceMethodFindById(-2L);
        checkCarServiceMethodFindById(0L);
        checkCarServiceMethodFindById(MAX_VALUE);
        checkCarServiceMethodFindById(MAX_VALUE - 1);
        checkCarServiceMethodFindById(MIN_VALUE);
        checkCarServiceMethodFindById(MIN_VALUE + 1);
    }

    @Test
    void findById_not_found_car() {

        Long id = 10L;
        car.setId(id);

        when(carRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy( () -> carService.findById(id)).
                isInstanceOf(NoSuchCarException.class).
                hasMessageContaining(format("Cannot find car with id %d", id));

        verify(carRepository, times(1)).findById(id);
    }

    @Test
    void findById_null() {

        Long id = null;
        when(carRepository.findById(id)).thenThrow(IllegalArgumentException.class);
        assertThatThrownBy( () -> carService.findById(id)).
                isInstanceOf(IllegalArgumentException.class);

        verify(carRepository, times(1)).findById(id);
    }

    private static void checkCarServiceMethodFindById(Long id){

        try {
            when(carRepository.findById(id)).thenReturn(Optional.of(car));
            Car carFormService = carService.findById(id);

            assertEquals(car, carFormService);
        } catch (NoSuchCarException e) {

            log.error(e.getMessage(), (Object) e.getStackTrace());
            fail();
        }

    }

    private static void checkCarServiceMethodDeleteById(Long id) {

        try {

            when(carRepository.findById(id)).thenReturn(Optional.of(car));
            carService.deleteById(id);
            verify(carRepository, times(1)).delete(any(Car.class));
            reset(carRepository);
        } catch (NoSuchCarException e) {

            log.error(e.getMessage(), (Object) e.getStackTrace());
            fail();
        }
    }
}