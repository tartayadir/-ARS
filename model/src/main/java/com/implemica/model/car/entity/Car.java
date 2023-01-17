package com.implemica.model.car.entity;

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

@Entity
@Table(name = "cars")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Car implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="brand")
    @Enumerated(EnumType.STRING)
    private CarBrands brand;

    @Column(name="model")
    private String model;

    @Enumerated(EnumType.STRING)
    @Column(name="car_body_types")
    private CarBodyTypes carBodyTypes;

    @Column(name="year")
    private short year;

    @Enumerated(EnumType.STRING)
    @Column(name="transmission_box_types")
    private TransmissionBoxTypes transmissionBoxTypes;

    @Column(name="engine_capacity")
    private double engineCapacity;

    @Column(name = "full_description", columnDefinition = "text")
    private String fullDescription;

    @Column(name = "short_description",columnDefinition = "text")
    private String shortDescription;

    @ElementCollection
    private List<String> additionalOptions;

    @Column(name = "image_file_name")
    private String imageFileName;
}