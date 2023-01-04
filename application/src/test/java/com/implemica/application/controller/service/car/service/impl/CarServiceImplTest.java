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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.utils.CarsUtils.generateRandomCar;
import static java.lang.Long.MAX_VALUE;
import static java.lang.Long.MIN_VALUE;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Slf4j
@ActiveProfiles("test")
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
                id(10L).
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
    void findAll() {

        List<Car> cars = List.of(
                generateRandomCar(),
                generateRandomCar(),
                generateRandomCar(),
                generateRandomCar(),
                generateRandomCar(),
                generateRandomCar()
        );
        checkFindAll(cars);

        cars = new ArrayList<>();
        checkFindAll(cars);
    }

    @Test
    void findById() {

        checkFindById(1L);
        checkFindById(-1L);
        checkFindById(2L);
        checkFindById(-2L);
        checkFindById(0L);
        checkFindById(MAX_VALUE);
        checkFindById(MAX_VALUE - 1);
        checkFindById(MIN_VALUE);
        checkFindById(MIN_VALUE + 1);
        checkFindById(22_525L);
        checkFindById(192L);
        checkFindById(99L);
        checkFindById(4_533L);
        checkFindById(11L);
        checkFindById(5L);
        checkFindById(7_877L);
    }

    @Test
    void findById_notFoundCar() {

        checkFindByIdCarNotFound(1L);
        checkFindByIdCarNotFound(-1L);
        checkFindByIdCarNotFound(2L);
        checkFindByIdCarNotFound(-2L);
        checkFindByIdCarNotFound(0L);
        checkFindByIdCarNotFound(MAX_VALUE);
        checkFindByIdCarNotFound(MAX_VALUE - 1);
        checkFindByIdCarNotFound(MIN_VALUE);
        checkFindByIdCarNotFound(MIN_VALUE + 1);
        checkFindByIdCarNotFound(22_525L);
        checkFindByIdCarNotFound(192L);
        checkFindByIdCarNotFound(99L);
        checkFindByIdCarNotFound(4_533L);
        checkFindByIdCarNotFound(11L);
        checkFindByIdCarNotFound(5L);
        checkFindByIdCarNotFound(7_877L);
    }

    @Test
    void findById_nullId() {

        Long id = null;
        assertThatThrownBy(() -> carService.findById(id)).
                isInstanceOf(IllegalArgumentException.class);

        verify(carRepository, times(0)).findById(id);
    }

    @Test
    void save() {

        checkSaveCar(car);
        checkSaveCar(generateRandomCar());
        checkSaveCar(generateRandomCar());
        checkSaveCar(generateRandomCar());
        checkSaveCar(generateRandomCar());
        checkSaveCar(generateRandomCar());
        checkSaveCar(generateRandomCar());
        checkSaveCar(generateRandomCar());
        checkSaveCar(generateRandomCar());
        checkSaveCar(generateRandomCar());
        checkSaveCar(generateRandomCar());
    }

    @Test
    void save_null_car() {

        car = null;
        when(carRepository.save(car)).thenThrow(IllegalArgumentException.class);
        assertThatThrownBy(() -> carService.save(car)).
                isInstanceOf(IllegalArgumentException.class);

        verify(carRepository, times(1)).save(car);
        reset(carRepository);
    }

    @Test
    void update() {

        checkUpdate(car);
        checkUpdate(generateRandomCar());
        checkUpdate(generateRandomCar());
        checkUpdate(generateRandomCar());
        checkUpdate(generateRandomCar());
        checkUpdate(generateRandomCar());
        checkUpdate(generateRandomCar());
        checkUpdate(generateRandomCar());
        checkUpdate(generateRandomCar());
        checkUpdate(generateRandomCar());
        checkUpdate(generateRandomCar());
        checkUpdate(generateRandomCar());
        checkUpdate(generateRandomCar());
    }

    @Test
    void update_not_found_car() {

        checkUpdateCarNotFound(1L, car);
        checkUpdateCarNotFound(-1L, car);
        checkUpdateCarNotFound(2L, car);
        checkUpdateCarNotFound(-2L, car);
        checkUpdateCarNotFound(0L, car);
        checkUpdateCarNotFound(MAX_VALUE, car);
        checkUpdateCarNotFound(MAX_VALUE - 1, car);
        checkUpdateCarNotFound(MIN_VALUE, car);
        checkUpdateCarNotFound(MIN_VALUE + 1, car);
        checkUpdateCarNotFound(22_525L, car);
        checkUpdateCarNotFound(192L, car);
        checkUpdateCarNotFound(99L, car);
        checkUpdateCarNotFound(4_533L, car);
        checkUpdateCarNotFound(11L, car);
        checkUpdateCarNotFound(5L, car);
        checkUpdateCarNotFound(7_877L, car);
    }

    @Test
    void update_id_null() {

        car.setId(null);
        assertThatThrownBy(() -> carService.update(car)).
                isInstanceOf(IllegalArgumentException.class).
                hasMessageContaining("Id cannot be null");
    }


    @Test
    void deleteById() {

        checkCarDeleteById(1L);
        checkCarDeleteById(-1L);
        checkCarDeleteById(2L);
        checkCarDeleteById(-2L);
        checkCarDeleteById(0L);
        checkCarDeleteById(MAX_VALUE);
        checkCarDeleteById(MAX_VALUE - 1);
        checkCarDeleteById(MIN_VALUE);
        checkCarDeleteById(MIN_VALUE + 1);
        checkCarDeleteById(22_525L);
        checkCarDeleteById(192L);
        checkCarDeleteById(99L);
        checkCarDeleteById(4_533L);
        checkCarDeleteById(11L);
        checkCarDeleteById(5L);
        checkCarDeleteById(7_877L);
    }

    @Test
    void deleteById_not_found_car() {

        checkCarDeleteByIdCarNotFound(1L);
        checkCarDeleteByIdCarNotFound(-1L);
        checkCarDeleteByIdCarNotFound(2L);
        checkCarDeleteByIdCarNotFound(-2L);
        checkCarDeleteByIdCarNotFound(0L);
        checkCarDeleteByIdCarNotFound(MAX_VALUE);
        checkCarDeleteByIdCarNotFound(MAX_VALUE - 1);
        checkCarDeleteByIdCarNotFound(MIN_VALUE);
        checkCarDeleteByIdCarNotFound(MIN_VALUE + 1);
        checkCarDeleteByIdCarNotFound(22_525L);
        checkCarDeleteByIdCarNotFound(192L);
        checkCarDeleteByIdCarNotFound(99L);
        checkCarDeleteByIdCarNotFound(4_533L);
        checkCarDeleteByIdCarNotFound(11L);
        checkCarDeleteByIdCarNotFound(5L);
        checkCarDeleteByIdCarNotFound(7_877L);
    }

    @Test
    void deleteById_null() {

        Long id = null;

        lenient().when(carRepository.findById(id)).thenThrow(IllegalArgumentException.class);
        lenient().doThrow(IllegalArgumentException.class).
                when(carRepository).
                delete(any(Car.class));

        assertThatThrownBy(() -> carService.deleteById(id)).
                isInstanceOf(IllegalArgumentException.class);
        verify(carRepository, times(0)).delete(any(Car.class));
    }

    private static void checkFindAll(List<Car> exceptedCars) {

        when(carRepository.findAll()).thenReturn(exceptedCars);
        assertEquals(exceptedCars, carService.findAll());

        verify(carRepository, times(1)).findAll();
        reset(carRepository);
    }

    private static void checkFindById(Long id) {

        try {
            when(carRepository.findById(id)).thenReturn(Optional.of(car));
            Car carFormService = carService.findById(id);

            assertEquals(car, carFormService);
            verify(carRepository, times(1)).findById(id);
            reset(carRepository);
        } catch (NoSuchCarException e) {

            log.error(e.getMessage(), (Object) e.getStackTrace());
            fail();
        }
    }

    private static void checkFindByIdCarNotFound(Long id) {

        given(carRepository.findById(id)).willAnswer(
                invocation -> {
                    throw new NoSuchCarException(format("Cannot find car with id %d", id));
                });
        assertThatThrownBy(() -> carService.findById(id)).
                isInstanceOf(NoSuchCarException.class).
                hasMessage(format("Cannot find car with id %d", id));

        verify(carRepository, times(1)).findById(id);
        reset(carRepository);
    }

    private static void checkSaveCar(Car car) {

        when(carRepository.save(car)).thenReturn(car);
        Car carFromService = carService.save(car);
        assertEquals(car, carFromService);

        verify(carRepository, times(1)).save(car);
        reset(carRepository);
    }

    private static void checkUpdate(Car updateCar) {

        try {

            when(carRepository.findById(updateCar.getId())).thenReturn(Optional.of(updateCar));
            when(carRepository.save(updateCar)).thenReturn(updateCar);

            Car carFromService = carService.update(updateCar);
            assertEquals(updateCar, carFromService);

            verify(carRepository, times(1)).save(updateCar);
            reset(carRepository);
        } catch (NoSuchCarException e) {

            log.error(e.getMessage(), (Object) e.getStackTrace());
            fail();
        }
    }

    private static void checkUpdateCarNotFound(Long carId, Car updateCar) {

        lenient().when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> carService.update(updateCar)).
                isInstanceOf(NoSuchCarException.class).
                hasMessageContaining(format("Cannot find and update car with id %d", updateCar.getId()));

//        verify(carRepository, times(1)).findById(carId);
        reset(carRepository);
    }

    private static void checkCarDeleteById(Long id) {

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

    private static void checkCarDeleteByIdCarNotFound(Long id) {

        when(carRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> carService.deleteById(id)).
                isInstanceOf(NoSuchCarException.class).
                hasMessageContaining(format("Cannot find car with id %d", id));

        verify(carRepository, times(1)).findById(id);
        reset(carRepository);
    }

}