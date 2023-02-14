import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {LoginService} from "../../../services/auth/login.service";

@Component({
  selector: 'app-add-car-button',
  templateUrl: './add-car-button.component.html',
})
export class AddCarButtonComponent {

  loginService: LoginService;

  constructor(
    private router: Router,
    loginService: LoginService
  ) {
    this.loginService = loginService;
  }

  goToAddPage($myParam: string = ''): void {
    const navigationDetails: string[] = ['/'];
    if($myParam.length) {
      navigationDetails.push($myParam);
    }
    this.router.navigate(navigationDetails);
  }
}
