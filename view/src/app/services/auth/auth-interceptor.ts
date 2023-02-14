import {HTTP_INTERCEPTORS, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {LoginService} from "./login.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {


  constructor(private loginService: LoginService) {
  }

  intercept(req: HttpRequest<any>,
            next: HttpHandler): Observable<HttpEvent<any>> {

    const idToken = localStorage.getItem("auth_tkn");
    console.log(this.loginService.isAuthenticated() + " is auth ")

    if (idToken && this.loginService.isAuthenticated()) {

      const cloned = req.clone({
        headers: req.headers.
        set("Authorization", "Bearer " + idToken)
      });

      return next.handle(cloned);
    }
    else {

      return next.handle(req);
    }
  }
}

export const httpInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
];
