package com.implemica.model.car.entity;

import java.util.List;
import java.util.Random;

public enum CarBrands {
    AUDI("Audi"), ACURA("Acura"), ALFA("Alfa"), ROMEO("Romeo"),
    ASTON_MARTIN("Aston Martin"), BENTLEY("Bentley"), BYD("BYD"), BMW("BMW"),
    BRILLIANCE("Brilliance"), BUICK("Buick"), BUGATTI("Bugatti"),
    CADILLAC("Cadillac"), CHANGAN("Changan"),  CHEVROLET("Chevrolet"),
    CHERY("Chery"), CHRYSLER("Chrysler"), CITROEN("Citroen"), DAEWOO("Daewoo"),
    DACIA("Dacia"), DAIHATSU("Daihatsu"), DODGE("Dodge"), FAW("FAW"),
    FERRARI("Ferrari"), FIAT("Fiat"), FORD("Ford"), GEELY("Geely"),
    GMC("GMC"), GREAT_WALL("Great Wall"), HONDA("Honda"), HUMMER("Hummer"),
    HYUNDAI("Hyundai"), INFINITI("Infiniti"), JAGUAR("Jaguar"), JEEP("JEEP"),
    KIA("KIA"), LAMBORGHINI("Lamborghini"), LAND_ROVER("Land Rover"),
    LANCIA("Lancia"), LEXUS("Lexus"), LIFAN("Lifan"), LINCOLN("Lincoln"),
    LOTUS("Lotus"), MARUSSIA("Marussia"), MAYBACH("Maybach"), MAZDA("Mazda"),
    MERCEDES("Mercedes"), MASERATI("Maserati"), MINI("Mini"), MCLAREN("Mclaren"),
    MITSUBISHI("Mitsubishi"), NISSAN("Nissan"), OPEL("Opel"), PEUGEOT("Peugeot"),

    PORSCHE("Porsche"), RENAULT("Renault"), SAAB("SAAB"), SEAT("Seat"),
    SKODA("Skoda"), SUBARU("Subaru"), SUZUKI("Suzuki"), TOYOTA("Toyota"),
    PONTIAC("Pontiac"), ROLLS_ROYCE("Rolls Royce"), SMART("Smart"),

    SSANGYONG("Ssanyong"), TESLA("Tesla"), VOLVO("Volvo"), DATSUN("Datsun"),
    VOLKSWAGEN("Volkswagen"), TAGAZ("Tagaz"), HAVAL_ROVER("Haval Rover"),
    GENESIS("Genesis");
    
    private final String stringValue;

    CarBrands(String stringValue) {
        this.stringValue = stringValue;
    }
    
    public String getStringValue(){
        return stringValue;
    }

    private static final List<CarBrands> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static CarBrands getRandomCarBrand()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
