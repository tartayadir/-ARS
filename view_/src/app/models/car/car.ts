import {CarBodyTypes} from "./CarBodyTypes";
import {TransmissionBoxTypes} from "./TransmissionBoxTypes";
import {CarBrands} from "./carBrands";

export class Car {
  id?: number;
  brand?: CarBrands;
  model?: string;
  carBodyTypes?: CarBodyTypes
  year?: number;
  transmissionBoxTypes?: TransmissionBoxTypes;
  engineCapacity?: number;
  fullDescription?: string;
  shortDescription?: string;
  additionalOptions: string[] = [];
  image?: string;
  imageFileName?: string;
}
