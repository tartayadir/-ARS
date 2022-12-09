import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {LoginInfo} from "../../../models/auth/LoginInfo";
import {LoginService} from "../../../services/auth/login.service";
import {ModalWindowService} from "../../../services/modal-window.service";
import {Title} from "@angular/platform-browser";
import {Location} from "@angular/common";

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html'
})
export class LoginPageComponent {

  loginInfo: LoginInfo = {
    username: "",
    password: ""
  };

  mouseIsOver: boolean = true;
  trySubmitted: boolean = false;
  regex: RegExp = new RegExp("^[a-zA-z0-9_]+$");

  constructor(private loginService: LoginService,
              private router: Router,
              private modalWindowService: ModalWindowService,
              private titleService: Title, private _location: Location) {
    this.titleService.setTitle("Login");
  }


  login(content: any) {

    this.trySubmitted = true;
    if (this.checkUserInfo()){
      this.loginService.login(this.loginInfo).subscribe(
        () => {
          if (this.loginService.isAuthenticated()){
            this._location.back();
          } else {
            this.router.navigate(["/"]).then(r => r);
          }
        });
    } else {

      console.log("Open error modal in login page")
      this.modalWindowService.open(content);
    }
  }

  private checkUserInfo() : boolean{

    let username: string = this.loginInfo.username as string;
    let password: string = this.loginInfo.password as string;

    let usernameIsValid: boolean =
      (this.loginInfo.username != undefined || this.loginInfo.username != null) &&
      username.length >= 2 && username.length <= 30 && this.regex.test(username);
    let passwordIsValid: boolean  =
      (this.loginInfo.password != undefined || this.loginInfo.password != null) &&
      password.length >= 8 && password.length <= 100 && this.regex.test(password);

    return usernameIsValid && passwordIsValid;
  }
}
