import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ErrorService} from "./error/error.service";
import {environment} from "../../environments/environment";
import {catchError, Observable, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  private apiServerURL = environment.apiServerURL;

  constructor(private http: HttpClient,
              private errorService: ErrorService) {
  }

  public uploadImage(image: File, imageId: string): Observable<any> {

    console.log("Http method - Post, image with name " + imageId);

    const uploadImageData = new FormData();
    uploadImageData.append('imageFile', image);

    return this.http.post<void>(`${this.apiServerURL}/image/${imageId}`,
      uploadImageData, {observe: 'response'}).pipe(
      catchError(this.errorHandler.bind(this))
    );
  }

  public deleteImage(imageId: string): Observable<any> {

    console.log("Http method - Delete, image with name " + imageId);

    return this.http.delete<void>(`${this.apiServerURL}/image/${imageId}`).pipe(
      catchError(this.errorHandler.bind(this))
    );
  }

  private errorHandler(error: HttpErrorResponse) {

    console.log("Error in ImageService")

    this.errorService.handlesWithStatus(error.status);
    return throwError(() => error.message);
  }
}
