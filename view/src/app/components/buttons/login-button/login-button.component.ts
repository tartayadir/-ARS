import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-login-button',
  templateUrl: './login-button.component.html'
})
export class LoginButtonComponent {

  constructor(
    private router: Router
  ) {
  }

  goToLoginPage($myParam: string = ''): void {
    const navigationDetails: string[] = ['/'];
    if($myParam.length) {
      navigationDetails.push($myParam);
    }
    this.router.navigate(navigationDetails);
  }
}
