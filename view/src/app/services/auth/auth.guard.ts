import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Router } from '@angular/router';

import {LoginService} from "./login.service";

@Injectable({
  providedIn: 'root'
})

export class AuthGuard implements CanActivate {

  private url!: string;
  constructor(private auth: LoginService, private router: Router) { }

  private authState(): boolean {
    if (this.isLoginOrRegister()) {
      this.router.navigate(['/']);
      return false;
    }
    return true;
  }
  private notAuthState(): boolean {
    if (this.isLoginOrRegister()) {
      return true;
    }
    this.router.navigate(['/login-page']);
    return false;
  }
  private isLoginOrRegister(): boolean {
    return this.url.includes('/login-page');
  }

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): any {
    this.url = state.url;
    if (this.auth.isAuthenticated()) {
      return this.authState();
    }
    return this.notAuthState();
  }
}
