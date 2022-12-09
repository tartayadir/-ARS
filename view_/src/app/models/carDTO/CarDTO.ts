import {CarBodyTypes} from "../car/CarBodyTypes";
import {TransmissionBoxTypes} from "../car/TransmissionBoxTypes";
import {CarBrands} from "../car/carBrands";

export class CarDTO {

  id!: number;
  brand!: CarBrands;
  model!: string;
  carBodyTypes!: CarBodyTypes
  year!: number;
  transmissionBoxTypes!: TransmissionBoxTypes;
  engineCapacity!: number;
  fullDescription!: string;
  shortDescription!: string;
  additionalOptions!: string[];
  imageFileName!: string;
}
