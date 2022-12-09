import { Component, OnInit } from '@angular/core';
import {ImageCroppedEvent} from "ngx-image-cropper";

@Component({
  selector: 'app-tesr',
  templateUrl: './tesr.component.html',
  styleUrls: ['./tesr.component.css']
})
export class TesrComponent {

  imgChangeEvt: any = '';
  cropImgPreview: any = '';

  onFileChange(event: any): void {
    this.imgChangeEvt = event;
  }
  cropImg(e: ImageCroppedEvent) {
    this.cropImgPreview = e.base64;
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

}
