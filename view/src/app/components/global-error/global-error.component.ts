import { Component, OnInit } from '@angular/core';
import {ErrorService} from "../../services/error/error.service";
import {ModalWindowService} from "../../services/modal-window.service";

@Component({
  selector: 'app-global-error',
  templateUrl: './global-error.component.html'
})
export class GlobalErrorComponent  implements OnInit {

  modalIsOpen: boolean = false;

  constructor(public errorService: ErrorService, private modalWindowService: ModalWindowService) { }

  ngOnInit(): void {
  }

  open(content: any) {
    this.modalWindowService.open(content);
  }

  closeModal(){
    this.modalIsOpen = false;
  }

  openModal(){
    this.modalIsOpen = true;
  }
}
