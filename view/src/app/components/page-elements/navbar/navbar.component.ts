import {Component, OnInit} from "@angular/core";
import {LoginService} from "../../../services/auth/login.service";
import {Router} from "@angular/router";

@Component({
  selector: "app-navbar",
  templateUrl: "navbar.component.html"
})
export class NavbarComponent implements OnInit{

  loginService: LoginService;
  isLoginPage!: boolean;
  isAddCarPage!: boolean;

  constructor(loginService: LoginService,private router: Router) {
    this.loginService = loginService;
  }

  ngOnInit(): void {
    this.router.events.subscribe((value) =>{
      this.isLoginPage = this.router.url.includes("/login-page");
      this.isAddCarPage = this.router.url.includes("/add-car-page");
    });
  }
}
