import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {map} from 'rxjs/operators';
import {AuthResponse} from "../../models/auth/AuthResponse";
import {ErrorService} from "../error/error.service";
import {JwtHelperService} from '@auth0/angular-jwt';
import {environment} from "../../../environments/environment";
import {AuthRequest} from "../../models/auth/AuthRequest";

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

  login(authRequest: AuthRequest): Observable<AuthResponse> {

    return this.http.post<AuthResponse>(`${this.apiServerURL}/authorization/login`, authRequest)
      .pipe(
        map((token: AuthResponse) => {
          return this.saveToken(token);
        }),
      catchError(this.errorHandler.bind(this))
    );
  }

  private saveToken(token: AuthResponse): AuthResponse {
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

    return !jwt.isTokenExpired(localStorage.getItem('auth_tkn') as string);
  }

  private errorHandler(error: HttpErrorResponse) {

    this.errorService.handlesWithStatus(error.status);
    return throwError(() => error.message);
  }
}
