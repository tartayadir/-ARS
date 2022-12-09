import { Component } from '@angular/core';
import {Location} from '@angular/common';

@Component({
  selector: 'app-come-back-button',
  templateUrl: './come-back-button.component.html',
  styleUrls: ['./come-back-button.component.css']
})
export class ComeBackButtonComponent {

  constructor(private _location: Location)
  {}

  backClicked() {
    this._location.back();
  }
}
