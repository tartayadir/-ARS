import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

import {Router} from "@angular/router";
import {CarsService} from "../../../services/cars.service";
import {ImageService} from "../../../services/image.service";
import {CarBodyTypes} from "../../../models/car/CarBodyTypes";
import {TransmissionBoxTypes} from "../../../models/car/TransmissionBoxTypes";
import {Car} from "../../../models/car/car";
import {ModalWindowService} from "../../../services/modal-window.service";
import {base64ToFile, ImageCroppedEvent} from "ngx-image-cropper";
import {ImagesService} from "../../../services/utils/images.service";
import {CarBrands} from "../../../models/car/carBrands";
import {Title} from "@angular/platform-browser";
import {MatChipInputEvent} from "@angular/material/chips";
import {COMMA, ENTER} from "@angular/cdk/keycodes";
import {Option} from "../../../models/option/Option";
import {LoginService} from "../../../services/auth/login.service";

@Component({
  selector: 'app-add-car-page',
  templateUrl: './add-car-page.component.html'
})
export class AddCarPageComponent implements OnInit{


  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  options: Option[] = [];

  imageTypeIsValid = true;
  imgChangeEvt: any = '';
  imageUrl: any;
  private image: string = '';
  imageIsUploaded: boolean = false;
  trySubmitted: boolean = false;
  tryChangeImage: boolean = false;
  private imageFile!: File;
  form: FormGroup;
  concurredYear!: number;

  ngOnInit(): void {

    this.loginService.isAuthenticated();
    this.concurredYear = new Date().getFullYear();
  }

  constructor(private carsService: CarsService, private router: Router, private imageService: ImageService,
              private formBuilder: FormBuilder, private modalWindowService: ModalWindowService,
              private images: ImagesService, private titleService: Title,private loginService: LoginService) {

    this.form = this.formBuilder.group({
      brand: [CarBrands.AUDI, [Validators.required]],
      model: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(40),
        Validators.pattern('[a-zA-Z\s ]+[a-zA-Z0-9\s ]*$')]],
      year: [2010, [Validators.required, Validators.min(1920), Validators.max(this.concurredYear),
        Validators.pattern("^[0-9]*$")]],
      engineCapacity: [1.0, [Validators.required, Validators.min(0.0), Validators.max(15.0)]],
      transmissionBoxTypes: [TransmissionBoxTypes.AUTOMATIC, [Validators.required]],
      carBodyTypes: [CarBodyTypes.COUPE, [Validators.required]],
      shortDescription: ['', [
        Validators.maxLength(1_000), Validators.pattern("^[a-zA-Z\s \n\r]+[a-zA-Z-\"/0-9\s \r\n.,:!?%()’‘\`\'—–-]*$")]],
      fullDescription: ['', [
        Validators.maxLength(5_000), Validators.pattern("^[a-zA-Z\s \n\r]+[a-zA-Z-\"/0-9\s \r\n.,:!?%()’‘\`\'—–-]*$")]],
    });
    this.titleService.setTitle("Add new car");
  }

  public addCar(content: any) {
    let car: Car = {
      additionalOptions: []
    };
    this.trySubmitted = true;

    if(this.formIsValid() && this.imageIsUploaded && this.imageTypeIsValid ||
      this.formIsValid() && !this.imageIsUploaded && this.imageTypeIsValid ){

      car.brand = this.form.value.brand as CarBrands;
      car.model = this.form.value.model as string;
      car.carBodyTypes = this.form.value.carBodyTypes as CarBodyTypes;
      car.year = this.form.value.year as number;
      car.transmissionBoxTypes = this.form.value.transmissionBoxTypes as TransmissionBoxTypes;
      car.engineCapacity = this.form.value.engineCapacity as number;
      car.shortDescription = this.form.value.shortDescription as string;
      if(car.shortDescription.length == 0){
        car.shortDescription = " "
      }
      car.fullDescription = this.form.value.fullDescription as string;
      if(car.fullDescription.length == 0){
        car.fullDescription = " "
      }
      console.log("fullDescription : " +  car.fullDescription);

      this.options.forEach((op: Option) => car.additionalOptions.push(op.name as string))

      if (this.imageIsUploaded){

        car.image = this.image as string;
        car.imageFileName = (car.model + car.brand + Math.floor(Math.random() * 1_000_000_000)).
        replace(" ", "");

        this.carsService.addCar(car).subscribe(
          responseCar => {
            car = responseCar;
            this.imageService.uploadImage(this.imageFile, car.imageFileName!).subscribe(
              () => {
                this.goToHomePage();
              },
              error => {
                this.carsService.deleteCar(car.id!, car.imageFileName!).subscribe();
              });
          }
        );
      } else {

        car.image = "";
        car.imageFileName = "default-car-image";

        this.carsService.addCar(car).subscribe(
          responseCar => {
            car = responseCar;
            this.goToHomePage();
          },
          error => {
            this.carsService.deleteCar(car.id!, car.imageFileName!).subscribe();
          });
      }


    } else {
      this.openModalWindow(content);
    }
  }

  openModalWindow(content: any) : void{
    this.modalWindowService.open(content);
  }

  onFileSelected(event: any) {

    if (event.target.files && event.target.files[0]) {

      let imageType: string = event.target.files[0].type;
      console.log("Image type : " + imageType);
      if(imageType == "image/png" || imageType == "image/gif" || imageType == "image/jpeg") {

        this.imageTypeIsValid = true
        this.tryChangeImage = true;

        this.imgChangeEvt = event;
        this.imageFile = event.target.files[0];

        this.trySubmitted = false;
        this.imageIsUploaded = true;

        let reader = new FileReader();
        reader.readAsDataURL(event.target.files[0]); // read file as data url
        reader.onload = (event) => { // called once readAsDataURL is completed
          this.imageUrl = (event.target as FileReader).result;
        }

      } else {

        this.imageTypeIsValid = false;
        this.trySubmitted = false;
        this.imageIsUploaded = true;
      }
    }
  }

  cropImg(event: ImageCroppedEvent) {

    this.imageUrl = event.base64;
    this.imageFile = this.images.blobToFile(base64ToFile(event.base64 as string), "");
  }

  imgLoad() {
    // display cropper tool
  }

  initCropper() {
    // init cropper
  }

  imgFailed() {
    // error msg
  }

  changeImageIsFalse(): void{

    this.tryChangeImage = false ;
  }

  private formIsValid() : boolean{

    console.log("Brand : " + this.form.controls.brand.valid);
    console.log("Model : " + this.form.controls.model.valid);
    console.log("year : " + this.form.controls.year.valid);
    console.log("engineCapacity : " + this.form.controls.engineCapacity.valid);
    console.log("transmissionBoxTypes : " + this.form.controls.transmissionBoxTypes.valid);
    console.log("carBodyTypes : " + this.form.controls.carBodyTypes.valid);
    console.log("fullDescription : " + this.form.controls.fullDescription.valid);
    console.log("shortDescription : " + this.form.controls.shortDescription.valid);

    return this.form.controls.brand.valid && this.form.controls.model.valid &&
    this.form.controls.year.valid && this.form.controls.engineCapacity.valid &&
    this.form.controls.transmissionBoxTypes.valid && this.form.controls.carBodyTypes.valid &&
    this.form.controls.fullDescription.valid && this.form.controls.shortDescription.valid;
  }


  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    if (value) {
      this.options.push({name: value});
    }

    event.chipInput!.clear();
  }

  remove(fruit: Option): void {
    const index = this.options.indexOf(fruit);

    if (index >= 0) {
      this.options.splice(index, 1);
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
