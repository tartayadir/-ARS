import {Component, Input, OnInit} from '@angular/core';
import {LoginService} from "../../../services/auth/login.service";
import {ModalWindowService} from "../../../services/modal-window.service";
import {CarsService} from "../../../services/cars.service";
import {Router} from "@angular/router";
import {CarsComponent} from "../../page-elements/cars/cars.component";
import {CarBrands} from "../../../models/car/CarBrands";

@Component({
  selector: 'app-confirm-delete-car-button',
  templateUrl: './confirm-delete-car-button.component.html',
  styleUrls: ['./confirm-delete-car-button.component.css']
})
export class ConfirmDeleteCarButtonComponent implements OnInit {

  @Input() parent?: CarsComponent;
  @Input() carId?: number;
  @Input() imageFileName?: string;
  @Input() model?: string;
  @Input() brand?: CarBrands;
  public loginService: LoginService;

  constructor(loginService: LoginService, private modalWindowService: ModalWindowService, private router: Router,
              private carService: CarsService) {
    this.loginService = loginService;
  }

  ngOnInit(): void {
  }

  public open(content: any) : void{

    this.modalWindowService.open(content);
  }

  public deleteCar() {

    if (this.parent == undefined){

      this.carService.deleteCar(this.carId!, this.imageFileName!).then(
        () => {
          this.goToHomePage()
        });
    }else {
      this.parent?.deleteCar(this.carId, this.imageFileName);
    }
  }

  goToHomePage($myParam: string = ''): void {
    const navigationDetails: string[] = ['/'];
    if ($myParam.length) {
      navigationDetails.push($myParam);
    }
    this.router.navigate(navigationDetails).then(r => r);
  }
}
