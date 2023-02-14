import {CarBodyTypes} from "./CarBodyTypes";
import {TransmissionBoxTypes} from "./TransmissionBoxTypes";
import {CarBrands} from "./CarBrands";

export class Car {
  id?: number;
  brand?: CarBrands;
  model?: string;
  carBodyType?: CarBodyTypes
  year?: number;
  transmissionBoxType?: TransmissionBoxTypes;
  engineCapacity?: number;
  fullDescription?: string;
  shortDescription?: string;
  additionalOptions: string[] = [];
  image?: string;
  imageFileName?: string;
}
