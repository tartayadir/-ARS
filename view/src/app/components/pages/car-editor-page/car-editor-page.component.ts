import {Component, OnInit} from '@angular/core';

import {HttpErrorResponse} from "@angular/common/http";
import {ActivatedRoute, Router} from "@angular/router";
import {ImageService} from "../../../services/image.service";
import {CarsService} from "../../../services/cars.service";
import {Car} from "../../../models/car/car";
import {ModalWindowService} from "../../../services/modal-window.service";
import {ImagesServiceConverter} from "../../../services/utils/images-service-converter.service";
import {base64ToFile, ImageCroppedEvent} from "ngx-image-cropper";
import {Title} from "@angular/platform-browser";
import {COMMA, ENTER} from "@angular/cdk/keycodes";
import {Option} from "../../../models/option/Option";
import {MatChipInputEvent} from "@angular/material/chips";
import {CarBrands} from "../../../models/car/CarBrands";
import {CarBodyTypes} from "../../../models/car/CarBodyTypes";


@Component({
  selector: 'app-car-editor',
  templateUrl: './car-editor-page.component.html'
})

export class CarEditorPageComponent implements OnInit {

  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  options: Option[] = [];

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    if (value) {
      this.options.push({name: value});
    }

    event.chipInput!.clear();
  }

  remove(option: Option): void {
    const index = this.options.indexOf(option);

    if (index >= 0) {
      this.options.splice(index, 1);
    }
  }

  imageTypeIsValid = true;
  imgChangeEvt: any = '';
  imageUrl: any;
  car!: Car;
  private image!: string;
  imageIsUploaded: boolean = false;
  trySubmitted: boolean = false;
  tryChangeImage: boolean = false;
  private imageFile!: File;
  withOptions: Array<ObjectWithOption> = new Array<ObjectWithOption>();
  concurredYear!: number;
  regexStringModel: string = "^[a-zA-Z\s ]+[a-zA-Z0-9\s ]*$";
  regexStringDescriptions: string = "^[a-zA-Z\s \n\r]+[a-zA-Z-\"/0-9\s \r\n.,:!?%()’‘\`\'—–-]*$";
  regexStringAddOptions: string = "^[a-zA-Z ]+[a-zA-Z–0-9 –-]*$";

  public brands = Object.keys(CarBrands);

  public carBodyTypes = Object.keys(CarBodyTypes);

  constructor(private carService: CarsService, private activateRoute: ActivatedRoute, private router: Router,
              private imageService: ImageService, private modalWindowService: ModalWindowService,
              private images: ImagesServiceConverter, private titleService: Title) {
  }

  ngOnInit(): void {
    let id: number = this.activateRoute.snapshot.params['id'];
    this.concurredYear = new Date().getFullYear();

    this.carService.getCar(id).subscribe(
      (response: Car) => {
        this.car = response;

        (this.car.additionalOptions).forEach((option: string) => {
          this.options.push({name: option})
        });

        this.titleService.setTitle("Edit " + this.getBrandValue() + " " + this.car.model)
      },
      (error: HttpErrorResponse) => {
        alert(error.message)
      }
    );
  }

  public getBrandEnumValue(value: string) : string{
    // @ts-ignore
    return CarBrands[value];
  }

  public getCarBodyTypesEnumValue(value: string) : string{
    // @ts-ignore
    return CarBodyTypes[value];
  }

  public getBrandValue(): string {
    return CarBrands[this.car!.brand!];
  }

  public async updateCar(content: any) {

    if (this.carIsValid() && this.imageTypeIsValid) {

      this.car.image = this.image as string;
      this.car.additionalOptions = [];
      this.options.forEach((option) => this.car.additionalOptions.push(option.name as string))

      if (this.imageIsUploaded) {

        let oldImageName: string = this.car.imageFileName as string;
        this.car.imageFileName = "" + this.car.brand + this.car.model + Math.floor(Math.random() * 1_000_000_000);
        this.car.imageFileName = this.car.imageFileName.replace(" ", "") + "."
          + this.imageFile.type.split("/")[1];

        const sleep = (ms: number) => new Promise(r => setTimeout(r, ms));

        this.imageService.uploadImage(this.imageFile, this.car.imageFileName as string).then( () => {

          sleep(1000);

          this.carService.update(this.car).then( () => {

            if(oldImageName != "default-car-image"){
              this.imageService.deleteImage(oldImageName);
            }

            this.goToHomePage();
          });
        });


      }else {

        this.carService.update(this.car).then(() => {
          this.goToHomePage();
        })
      }
    } else {
      this.modalWindowService.open(content);
    }
  }

  addOption() {
    if (this.withOptions.length < 10) {
      this.withOptions.push({option: ""});
    }
  }

  removeOption(i: number) {

    this.withOptions.splice(i, 1);
    if (this.withOptions.length == 0) {
      this.withOptions.push(new ObjectWithOption(""));
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
    this.images.dataURItoBlob(event.base64 as string).subscribe( blob => {
      this.imageFile = this.images.blobToFile(blob, "");
    })
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

  carIsValid(): boolean {

    let model: string = this.car.model as string;
    let year: number = this.car.year as number;
    let engineCapacity: number = this.car.engineCapacity as number;
    let fullDescription: string = this.car.fullDescription as string;
    let shortDescription: string = this.car.shortDescription as string;
    let additionalOptions: string[] = this.withOptions.map(u => u.option);
    let carIsValid: boolean = true;

    shortDescription = shortDescription.length == 0 ? "" : shortDescription;
    fullDescription = fullDescription.length == 0 ? "" : fullDescription;

    let regexDescriptions: RegExp = new RegExp(this.regexStringDescriptions);
    let regexModel: RegExp = new RegExp(this.regexStringModel);
    let regexAddOptions: RegExp = new RegExp(this.regexStringAddOptions);

    console.log("Model regex test : " + regexModel.test(model));
    console.log("Model : " + model);
    carIsValid = carIsValid && model.length >= 2 && model.length <= 40 && regexModel.test(model);
    console.log("Model is " + carIsValid);
    carIsValid = carIsValid && year >= 1_920 && year <= this.concurredYear && (year != null && true) && year % 1 == 0;
    console.log("Year is " + carIsValid);
    carIsValid = carIsValid && engineCapacity >= 0.0 && engineCapacity <= 15.0 && (engineCapacity != null && true);
    console.log("engineCapacity is " + carIsValid);
    console.log("engineCapacity is " + engineCapacity);
    carIsValid = carIsValid && shortDescription.length <= 1_000 &&
      (regexDescriptions.test(shortDescription));
    console.log("shortDescription is " + carIsValid);
    carIsValid = carIsValid && fullDescription.length <= 5_000 &&
      (regexDescriptions.test(fullDescription));
    console.log("fullDescription is " + carIsValid);
    additionalOptions.filter(option => option.length > 0 && option != "undefined").forEach(option => carIsValid =
      regexAddOptions.test(option) && carIsValid && option.length <= 20 && option.length >= 2);
    additionalOptions.filter(option => option.length > 0 && option != "undefined").forEach(option => console.log("" + regexAddOptions.test(option) + " " + (option.length <= 20) + " " + (option.length >= 2))
    )
    console.log("additionalOptions is " + carIsValid);

    return carIsValid;
  }

  goToHomePage($myParam: string = ''): void {
    const navigationDetails: string[] = ['/'];
    if ($myParam.length) {
      navigationDetails.push($myParam);
    }
    this.router.navigate(navigationDetails).then(r => r);
  }
}

class ObjectWithOption {
  option: string;


  constructor(option: string) {
    this.option = option;
  }
}
