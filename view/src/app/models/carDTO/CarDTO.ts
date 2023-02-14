import {CarBodyTypes} from "../car/CarBodyTypes";
import {TransmissionBoxTypes} from "../car/TransmissionBoxTypes";
import {CarBrands} from "../car/CarBrands";

export class CarDTO {

  id!: number;
  brand!: CarBrands;
  model!: string;
  carBodyTypes!: CarBodyTypes | undefined
  year!: number;
  transmissionBoxTypes!: TransmissionBoxTypes;
  engineCapacity!: number;
  fullDescription!: string;
  shortDescription!: string;
  additionalOptions!: string[];
  imageFileId!: string;
}
