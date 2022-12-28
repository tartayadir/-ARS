import {Component, OnInit} from "@angular/core";

import {HttpErrorResponse} from "@angular/common/http";
import {CarsService} from "../../../services/cars.service";
import {Car} from "../../../models/car/car";
import {ActivatedRoute, Router} from "@angular/router";
import {ImageService} from "../../../services/image.service";
import {Title} from "@angular/platform-browser";
import {LoginService} from "../../../services/auth/login.service";
import {CarBrands} from "../../../models/car/CarBrands";

@Component({
  selector: "app-cars",
  styleUrls: ["./cars.component.css"],
  templateUrl: "./cars.component.html"
})

export class CarsComponent implements OnInit {

  cars: Car[] = [];
  isReadMore: boolean[] = [];

  showText(i: number): void {
    this.isReadMore[i] = !this.isReadMore[i];
  }

  constructor(private carService: CarsService, private router: Router, private route: ActivatedRoute,
              private imageService: ImageService, private titleService: Title, public loginService: LoginService) {
  }

  ngOnInit(): void {
    this.getCars();
  }

  public getCars(): void {
    this.carService.getCars().subscribe(
      (response: Car[]) => {
        this.cars = response;
        this.cars.forEach(car => {
          this.isReadMore.push(true);
        })
        this.titleService.setTitle("Car Catalog");
      },
      (error: HttpErrorResponse) => {
        alert(error.message)
      }
    )
  }

  public deleteCar(id?: number, imageFileName?: string): void {

    this.carService.deleteCar(id!, imageFileName!).subscribe(
      () => {
        this.ngOnInit();
        this.goToHomePage()
      })
  }

  goToHomePage($myParam: string = ''): void {
    const navigationDetails: string[] = ['/'];
    if ($myParam.length) {
      navigationDetails.push($myParam);
    }
    this.router.navigate(navigationDetails).then(r => r);
  }

  public getCarBrand(i: number): string {

    return CarBrands[this.cars[i].brand!];
  }
}
