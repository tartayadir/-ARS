import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {map} from 'rxjs/operators';

import {LoginInfo} from "../../models/auth/LoginInfo";
import {JwtResponse} from "../../models/auth/JwtResponse";
import {ErrorService} from "../error/error.service";
import * as moment from 'moment';
import {JwtHelperService} from '@auth0/angular-jwt';
import {environment} from "../../../environments/environment";

const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/x-www-form-urlencoded'})
};

const jwt = new JwtHelperService();

class DecodedToken {
  exp!: number;
  sub!: string;
  roles!: string[];
}

@Injectable({
  providedIn: 'root'
})

export class LoginService {

  private decodedToken;

  private apiServerURL = environment.apiServerURL;

  constructor(private http: HttpClient, private errorService: ErrorService) {
    this.decodedToken = JSON.parse(localStorage.getItem('auth_meta')!) || new DecodedToken();
  }

  login(loginInfo: LoginInfo): Observable<JwtResponse> {

    let body = new URLSearchParams();
    body.set('username', loginInfo.username as string);
    body.set('password', loginInfo.password as string);

    return this.http.post<JwtResponse>(`${this.apiServerURL}/authorization/login`, body, httpOptions)
      .pipe(
        map((token: JwtResponse) => {
          return this.saveToken(token);
        }),
      catchError(this.errorHandler.bind(this))
    );
  }

  private saveToken(token: JwtResponse): JwtResponse {
    this.decodedToken = jwt.decodeToken(token.access_token);

    localStorage.setItem('auth_tkn', token.access_token);
    localStorage.setItem('auth_sub', this.decodedToken.sub);
    localStorage.setItem('auth_role', this.decodedToken.roles[0]);
    localStorage.setItem('auth_meta', JSON.stringify(this.decodedToken));
    return token;
  }

  public logout(): void {
    localStorage.removeItem('auth_tkn');
    localStorage.removeItem('auth_sub');
    localStorage.removeItem('auth_role');
    localStorage.removeItem('auth_meta');

    this.decodedToken = new DecodedToken();

    location.reload();
  }

  public getUsername(): string {
    return localStorage.getItem('auth_sub')!;
  }

  public isAdmin(): boolean {
    return localStorage.getItem('auth_role') == 'ADMIN_ROLE';
  }

  public isAuthenticated(): boolean {

    // console.log(" " + jwt.isTokenExpired(localStorage.getItem('auth_tkn') as string));
    // console.log(moment().isBefore(moment.unix(this.decodedToken.exp)));
    // console.log(moment.now())
    // console.log(this.decodedToken.exp);
    // console.log(moment.now() >= this.decodedToken.exp)

    return !jwt.isTokenExpired(localStorage.getItem('auth_tkn') as string);
  }

  private errorHandler(error: HttpErrorResponse) {

    this.errorService.handlesWithStatus(error.status);
    return throwError(() => error.message);
  }
}
