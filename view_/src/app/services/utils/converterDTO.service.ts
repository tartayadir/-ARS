import {Injectable} from "@angular/core";
import {Car} from "../../models/car/car";
import {CarDTO} from "../../models/carDTO/CarDTO";
import {CarBodyTypes} from "../../models/car/CarBodyTypes";
import {TransmissionBoxTypes} from "../../models/car/TransmissionBoxTypes";
import {CarBrands} from "../../models/car/carBrands";

@Injectable({
  providedIn: "root"
})

export class ConverterDTO {

  private s3URL : string = "https://carsbucketspringboot.s3.eu-west-3.amazonaws.com/";

  constructor() {}

  public carEntityToCarDTO(car: Car) : CarDTO {

    let carDTO: CarDTO = new CarDTO();

    carDTO.id = car.id as number;
    carDTO.brand = car.brand as CarBrands;
    carDTO.model = car.model as string;
    carDTO.carBodyTypes = car.carBodyTypes as CarBodyTypes;
    carDTO.transmissionBoxTypes = car.transmissionBoxTypes as TransmissionBoxTypes;
    carDTO.year = car.year as number;
    carDTO.engineCapacity = car.engineCapacity as number;
    carDTO.fullDescription = car.fullDescription as string;
    carDTO.shortDescription = car.shortDescription as string;
    carDTO.additionalOptions = car.additionalOptions as string[];
    carDTO.imageFileId = car.imageFileName as string;

    return carDTO;
  }

  public CarDTOToCarEntity(carDTO: CarDTO) : Car {

    let car: Car = new Car();

    car.id = carDTO.id;
    car.brand = carDTO.brand;
    car.model = carDTO.model;
    car.carBodyTypes = carDTO.carBodyTypes;
    car.transmissionBoxTypes = carDTO.transmissionBoxTypes;
    car.year = carDTO.year;
    car.engineCapacity = carDTO.engineCapacity;
    car.fullDescription = carDTO.fullDescription;
    car.shortDescription = carDTO.shortDescription;
    car.additionalOptions = carDTO.additionalOptions;
    car.imageFileName = carDTO.imageFileId;

    car.image = this.s3URL + car.imageFileName;

    return car;
  }
}
