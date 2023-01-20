import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {Car} from "../models/car/car";
import {environment} from "../../environments/environment";
import {ErrorService} from "./error/error.service";
import {CarDTO} from "../models/carDTO/CarDTO";
import {map} from "rxjs/operators";
import {ConverterDTO} from "./utils/converterDTO.service";
import {Router} from "@angular/router";

@Injectable({
  providedIn: "root"
})

export class CarsService {

  private apiServerURL =  environment.apiServerURL;

  constructor(
    private http: HttpClient,
    private errorService: ErrorService,
    private converterDTO: ConverterDTO,
    private router: Router
  ) {
  }

  public getCars(): Observable<Car[]> {

    console.log("Http method - GET, all cars");

    return this.http.get<CarDTO[]>(`${this.apiServerURL}/car-catalog`).pipe(
      map((carDTOs: CarDTO[]) => carDTOs.
      sort((a, b) => a.id - b.id).
      map(dto => this.converterDTO.CarDTOToCarEntity(dto))),
      catchError(this.errorHandler.bind(this))
    );
  }

  public deleteCar(carId: number, imageId: string) {

    console.log("Http method - DELETE, car with id " + carId + " " + imageId);

    return this.http.delete<void>(`${this.apiServerURL}/car-catalog/${carId}`,{
      params: {
        imageId: imageId,
      }}).pipe(
      catchError(this.errorHandler.bind(this))
    );
  }

  public getCar(carId: number): Observable<Car> {

    console.log("Http method - Get, car with id " + carId);

    return  this.http.get<CarDTO>(`${this.apiServerURL}/car-catalog/${carId}`).pipe(
      map(dto => this.converterDTO.CarDTOToCarEntity(dto)),
      catchError(this.errorHandler.bind(this))
    );
  }

  public async update(car: Car) {

    console.log("Http method - PUT, car " + car);

    let carDTO: CarDTO = this.converterDTO.carEntityToCarDTO(car);

    return await this.http.put<void>(`${this.apiServerURL}/car-catalog`, carDTO).
    toPromise().catch(this.errorHandler.bind(this));
  }

  public async addCar(car: Car) {

    console.log("Http method - POST, car " + car);

    let carDTO: CarDTO = this.converterDTO.carEntityToCarDTO(car);

    return await this.http.post<CarDTO>(`${this.apiServerURL}/car-catalog`, carDTO).
    toPromise().
    catch(this.errorHandler.bind(this));
  }

  private errorHandler(error: HttpErrorResponse) {

    console.log("Error in CarService");
    console.log("status : " + error.status);

    this.errorService.handlesWithStatus(error.status);
    this.router.navigate(["/"]).then(r => r);
    return throwError(() => error.message);
  }
}
