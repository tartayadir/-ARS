import {Component, OnInit} from '@angular/core';

import {ActivatedRoute} from "@angular/router";
import {Car} from "../../../models/car/car";
import {CarsService} from "../../../services/cars.service";
import {Title} from "@angular/platform-browser";
import {CarBodyTypes} from "../../../models/car/CarBodyTypes";
import {CarBrands} from "../../../models/car/CarBrands";

@Component({
  selector: 'app-car-details',
  templateUrl: './car-details-page.component.html'
})
export class CarDetailsPageComponent implements OnInit {

  public car: Car  = {
    additionalOptions: []
  };

  constructor(private carService: CarsService, private activateRoute: ActivatedRoute,
              private titleService: Title
  ) {}

  ngOnInit(): void {
    let id: number = this.activateRoute.snapshot.params['id'];
    console.log("car id on details page : " + id)

    this.carService.getCar(id).subscribe(
      (response: Car) =>{
        this.car = response;
        this.titleService.setTitle(this.getCarBrand() + " " + this.car.model)
      }
    );

  }

  hasFullDescription(): boolean {

    let description: string = this.car.fullDescription!;
    return description!.length! > 1 && description != " ";
  }

  hasShortDescription(): boolean {

    let description: string = this.car.shortDescription!;
    return description!.length! > 1 && description != " ";
  }

  public getCarBodyType(): string {
    // @ts-ignore
    return CarBodyTypes[this.car!.carBodyType!];
  }

  public getCarBrand(): string {
    return CarBrands[this.car!.brand!];
  }
}
