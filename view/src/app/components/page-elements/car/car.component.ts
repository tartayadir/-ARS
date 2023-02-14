import {Component, Input} from '@angular/core';
import {Car} from "../../../models/car/car";

@Component({
  selector: 'app-car',
  templateUrl: './car.component.html'
})

export class CarComponent {
  @Input() car: Car = {
    additionalOptions: []
  }
}
