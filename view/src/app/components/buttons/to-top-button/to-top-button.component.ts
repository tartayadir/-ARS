import {Component} from '@angular/core';
import {ViewportScroller} from "@angular/common";

@Component({
  selector: 'app-to-top-button',
  templateUrl: './to-top-button.component.html',
  styleUrls: ['./to-top-button.component.css']
})


export class ToTopButtonComponent{

  position: Array<number> = [];

  constructor(public viewPortScroller: ViewportScroller) {
  }


  public onFloatClick() {

    this.viewPortScroller.getScrollPosition();
    this.viewPortScroller.scrollToPosition([0, 0]);
  }
}
