import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {LoginService} from "../../../services/auth/login.service";

@Component({
  selector: 'app-logout-button',
  templateUrl: './logout-button.component.html'
})
export class LogoutButtonComponent {

  constructor(
    private router: Router,
    private loginService: LoginService
  ) { }

  logout($myParam: string = ''): void {

    this.loginService.logout();

    const navigationDetails: string[] = ['/'];
    if($myParam.length) {
      navigationDetails.push($myParam);
    }
    this.router.navigate(navigationDetails);
  }
}
