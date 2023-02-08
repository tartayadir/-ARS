package com.implemica.model.car.enums;

import com.implemica.model.car.entity.Car;
import lombok.AllArgsConstructor;

/**
 * The thing is the type of car brand contains a field which is a standard entry
 * of the name of the brand. This is used in {@link Car} for typing.
 */
@AllArgsConstructor
public enum CarBrand {
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

    /**
     * Standard entry for brand name.
     */
    private final String standardName;

    /**
     * Getter for standard entry.
     *
     * @return standard entry of car brand
     */
    public String getStringValue() {
        return standardName;
    }
}
