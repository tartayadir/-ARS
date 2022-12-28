import {Component, Input} from '@angular/core';
import {LoginService} from "../../../services/auth/login.service";
import {CarBrands} from "../../../models/car/CarBrands";

@Component({
  selector: 'app-edit-car-button',
  templateUrl: './edit-car-button.component.html'
})
export class EditCarButtonComponent {

  @Input() carId?: number;
  @Input() carBrand?: CarBrands;
  @Input() carModel?: string;
  loginService: LoginService

  constructor(loginService: LoginService) {
    this.loginService = loginService;
  }
}
