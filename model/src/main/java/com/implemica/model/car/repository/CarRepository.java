package com.implemica.model.car.repository;

import com.implemica.model.car.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * DAO for car entity {@link Car} that use id type {@link Long}
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}