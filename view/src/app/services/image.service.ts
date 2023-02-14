import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ErrorService} from "./error/error.service";
import {environment} from "../../environments/environment";
import {throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  private apiServerURL = environment.apiServerURL;

  constructor(private http: HttpClient,
              private errorService: ErrorService) {
  }

  public async uploadImage(image: File, imageId: string) {

    console.log("Http method - Post, image with name " + imageId);

    const uploadImageData = new FormData();
    uploadImageData.append('imageFile', image);

    return await this.http.post<void>(`${this.apiServerURL}/image/${imageId}`,
      uploadImageData, {observe: 'response'}).
    toPromise().
    catch(this.errorHandler.bind(this));
  }

  public async deleteImage(imageId: string) {

    console.log("Http method - Delete, image with name " + imageId);

    return await this.http.delete<void>(`${this.apiServerURL}/image/${imageId}`).
    toPromise().catch(this.errorHandler.bind(this));
  }

  private errorHandler(error: HttpErrorResponse) {

    console.log("Error in ImageService")

    this.errorService.handlesWithStatus(error.status);
    return throwError(() => error.message);
  }
}
