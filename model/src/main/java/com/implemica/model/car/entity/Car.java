package com.implemica.model.car.entity;

import lombok.*;

import javax.persistence.*;
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
    private Long id;

    @Enumerated(EnumType.STRING)
    private CarBrands brand;

    private String model;

    @Enumerated(EnumType.STRING)
    private CarBodyTypes carBodyTypes;

    private short year;

    @Enumerated(EnumType.STRING)
    private TransmissionBoxTypes transmissionBoxTypes;

    private double engineCapacity;

    @Column(columnDefinition = "text")
    private String fullDescription;

    @Column(columnDefinition = "text")
    private String shortDescription;

    @ElementCollection
    private List<String> additionalOptions;

    private String imageFileName;
}