import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'view_';

  constructor(private router: Router) {
  }

  ngOnInit() {

    window.addEventListener('storage', (event) => {

      if (event.storageArea == localStorage) {
        let token = localStorage.getItem('auth_tkn');

        if (this.router.url == "/"){
          window.location.reload();
        } else {
          this.router.navigate(['/']);
        }
      }
    }, false);

  }

}
