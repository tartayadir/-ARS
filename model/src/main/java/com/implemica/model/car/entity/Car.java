package com.implemica.model.car.entity;

import com.implemica.model.car.enums.CarBodyType;
import com.implemica.model.car.enums.CarBrand;
import com.implemica.model.car.enums.TransmissionBoxType;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ElementCollection;
import java.io.Serializable;
import java.util.List;

/**
 * The domain model is a representation of the car and contains the basic
 * characteristics. To present some fields to be used enums: {@link TransmissionBoxType},
 * {@link CarBodyType}, {@link CarBrand}; and written as a string to the database, also
 * for the list of additional options to use the ratio one to many and the separated table,
 * as an external key to use the car id.
 */
@Entity
@Table(name = "cars")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Car implements Serializable {

    /**
     * Car primary key that used to search the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    /**
     * Brand car is {@link CarBrand} enum element, fonds in the database as a string.
     */
    @Column(name="brand")
    @Enumerated(EnumType.STRING)
    private CarBrand brand;

    /**
     * Car brand model.
     */
    @Column(name="model")
    private String model;

    /**
     * Car body type is {@link CarBodyType} enum element, fonds in the database
     * as a string.
     */
    @Enumerated(EnumType.STRING)
    @Column(name="car_body_types")
    private CarBodyType carBodyTypes;

    /**
     * The year the car was made.
     */
    @Column(name="year")
    private short year;

    /**
     * Car transmission box type is {@link TransmissionBoxType} enum element, fonds in the database as a string
     */
    @Enumerated(EnumType.STRING)
    @Column(name="transmission_box_types")
    private TransmissionBoxType transmissionBoxTypes;

    /**
     * The volume of the engine block of the car is measured in liters of cubic.
     */
    @Column(name="engine_capacity")
    private double engineCapacity;

    /**
     * Full description of the car that is stored in the database as text, because
     * its length can weigh 255 characters.
     */
    @Column(name="full_description", columnDefinition = "text")
    private String fullDescription;

    /**
     * Short description of the car that is stored in the database as text,
     * because its length can weigh 255 characters.
     */
    @Column(name="short_description",columnDefinition = "text")
    private String shortDescription;

    /**
     *  List of additional options to use the ratio one to many and the separated table,
     *  as an external key to use the car id.
     */
    @ElementCollection
    private List<String> additionalOptions;

    /**
     * The name of the picture in the bucket on the AWS that refers to the car.
     * It will search for the file and also be used to remove it when removing the car.
     */
    @Column(name="image_file_name")
    private String imageFileName;
}