package com.implemica.model.car.dto;

import com.implemica.model.car.entity.CarBodyTypes;
import com.implemica.model.car.entity.CarBrands;
import com.implemica.model.car.entity.TransmissionBoxTypes;
import com.implemica.model.enum_validators.ValueOfEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("CarDTO")
public class CarDTO implements Serializable {

    @ApiModelProperty(name = "Car ID", position = 1, example = "10", notes = "Car ID that is used for find car in data base.")
    @Min(value = 0, message = "Id cannot be less than 1.")
    private Long id;

    @ApiModelProperty(name = "Car brand", position = 2, example = "MAZDA",
            dataType = "com.implemica.model.car.entity.CarBrands",
            notes = "Car brand refers to the basic concept. Otherwise, can be called as a brand or trademark.")
    @ValueOfEnum(enumClass = CarBrands.class, message = "Car brand is invalid.")
    @NotEmpty(message = "Brand is required and cannot be empty or null.")
    private String brand;

    @ApiModelProperty(notes = "The model describes the type of vehicle (what type of body) produced under a certain brand.",
            position = 3, example = "RX 8")
    @NotEmpty(message = "Model is required and cannot be empty or null.")
    @Size(min = 2, max = 40, message = "Model must be greater than 2 and less then 30.")
    @Pattern(regexp = "^[a-zA-Z ]+[a-zA-Z0-9 ]*$", message = "Model must not contain anything other than letters.")
    private String model;

    @ApiModelProperty(notes = "It is an element of the car design that determines the size, type and capacity of the car to carry goods and passengers.",
            position = 4, example = "PICKUP", dataType = "com.implemica.model.car.entity.CarBodyTypes")
    @ValueOfEnum(enumClass = CarBodyTypes.class, message = "Car bode type is invalid.")
    @NotEmpty(message = "Car body type is required and cannot be empty or null.")
    private String carBodyTypes;

    @ApiModelProperty(notes = "The year of issue is indicated by one digit, which corresponds to the last digit of the calendar year (similar to VIN).", position = 5, example = "2004")
    @NotNull(message = "Produce year is required and cannot be empty or null.")
    @Range(min = 1920, max = 2022, message = "Prodice year must be greater than 1920 and less than 2022.")
    private Short year;

    @ApiModelProperty(notes = "is a collection of different units and mechanisms that transmit torque from the engine to the driving wheels and change it in size and direction.", position = 6, example = "ROBOTIC",
            dataType = "com.implemica.model.car.entity.TransmissionBoxTypes")
    @ValueOfEnum(enumClass = TransmissionBoxTypes.class, message = "Transmission box type is invalid.")
    @NotEmpty(message = "Transmission box type is required and cannot be empty or null.")
    private String transmissionBoxTypes;

    @ApiModelProperty(notes = "The engine volume is defined as the total volume of all cylinders, or the volume of one cylinder multiplied by their number.", position = 7, example = "3.5")
    @DecimalMin(value = "0.0", message = "Engine capacity must be positive number or 0.")
    @DecimalMax(value = "15.0", message = "Engine capacity must be less then 15,0.")
    @NotNull(message = "Engine capacity is required.")
    private Double engineCapacity;

    @ApiModelProperty(example = "A mid-sized coupe, it launched in 2003, just as mid-sized coupes were kinda dying out." +
            " The asteroid was certainly on its way, at least. Rewind to the Nineties and this was a bustling corner " +
            "of the market. But sitting and naming some of the numerous mainstream examples – Calibra, Cougar, Prelude," +
            " Coupe (not every carmaker was imaginative) – only makes you realise how long ago this chapter closed. The" +
            " rise of the SUV didn’t only claim the four-door repmobile’s popularity.\n" +
            "\n" +
            "But the RX-8 was never really billed as a rival to mainstream coupes, even if its ample practicality and" +
            " modest power output placed it right on their turf. It instead launched in tandem with the Nissan 350Z to " +
            "join the Honda S2000 in the 'attainable Japanese sports car' sub-niche. These were cars riding the wave of" +
            " The Fast and the Furious’ success, bringing a bit of JDM sparkle to early Noughties Britain.",
            position = 8, notes = "It is the full description of car that contains some details about car.")
    @Size( max = 5000, message = "Full description must be less then 5000.")
    @Pattern(regexp = "^[a-zA-Z \n\r]+[a-zA-Z-\"/0-9 \n.,:!?%()`’‘'—–-]*$",
            message = "The full description should not contain anything but a word," +
                    " numbers and punctuation marks, also cannot start with number or punctuation mark.")
    private String fullDescription;

    @ApiModelProperty(example = "Rarely has a car broken with convention in such a wanton manner. The RX-8 appears to be " +
            "everything cars today aren’t, and indeed it seemed to be swimming upstream for a fair few years" +
            " of its existence.", position = 9, notes = "It is the short description of car that describe car in general.")
    @Size( max = 1000, message = "Short description must be less then 1000.")
    @Pattern(regexp = "^[a-zA-Z \n\r]+[a-zA-Z-\"/0-9 \n.,:!?%()`’‘'—–-]*$",
            message = "The short description should not contain anything but a word," +
                    " numbers and punctuation marks, also cannot start with number or punctuation mark.")
    private String shortDescription;

    @ApiModelProperty(notes = "Additional options allow you to equip the selected package already needed by the owner list of features.", position = 10,
            example = "[\"Infotainment\",\"Advanced driver aids\",\"Extended Warranties\",\"Keyless entry and ignition\"]")
    private List<String> additionalOptions;

    @ApiModelProperty(notes = "Car image file name that you can find at AWS S# by it.", position = 11, example = "default-car-image")
    @NotEmpty(message = "Image file name is required and cannot be empty or null.")
    @Size(min = 10, max = 150, message = "Image file name must be greater than 10 and less then 150.")
    private String imageFileId;

    public void setEngineCapacity(double engineCapacity) {
        this.engineCapacity = engineCapacity;
    }
}
