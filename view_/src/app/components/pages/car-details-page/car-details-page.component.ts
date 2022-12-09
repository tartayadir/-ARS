import {Component, OnInit} from '@angular/core';

import {ActivatedRoute} from "@angular/router";
import {Car} from "../../../models/car/car";
import {CarsService} from "../../../services/cars.service";
import {Title} from "@angular/platform-browser";
import {CarBrands} from "../../../models/car/carBrands";

@Component({
  selector: 'app-car-details',
  templateUrl: './car-details-page.component.html'
})
export class CarDetailsPageComponent implements OnInit
  {

  public car: Car  = {
    brand: CarBrands.AUDI,
    additionalOptions: []
  };

  constructor(private carService: CarsService, private activateRoute: ActivatedRoute,
              private titleService: Title
  ) {
  }

  ngOnInit(): void {
    let id: number = this.activateRoute.snapshot.params['id'];

    this.carService.getCar(id).subscribe(
      (response: Car) =>{
        this.car = response;
        this.titleService.setTitle(this.car.brand!.toString() + " " + this.car.model)
      }
    );

  }

  hasDescription(): boolean {

    let description: string = this.car.fullDescription!;
    return description.length > 1 && description != " ";
  }
}
