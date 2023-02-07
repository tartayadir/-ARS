import {Component, Input} from '@angular/core';
import {LoginService} from "../../../services/auth/login.service";
import {CarsService} from "../../../services/cars.service";

@Component({
  selector: 'app-delete-car-button',
  templateUrl: './delete-car-button.component.html'
})
export class DeleteCarButtonComponent {

  @Input() carId?: number;
  @Input() imageFileName?: string;
  loginService: LoginService

  constructor(loginService: LoginService, private carsService: CarsService) {
    this.loginService = loginService;
  }

  public deleteCar() {

    this.carsService.deleteCar(this.carId!, this.imageFileName!).then( () => {

      window.location.reload();
    });
  }
}
