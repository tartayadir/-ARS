import { Injectable } from '@angular/core';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  error$ = new Subject<string>();

  handleWithMassage(message: string){
    this.error$.next(message);
  }

  handlesWithStatus(status: number){

    let errorMessage: string;

    switch (status){
      case 400 :
        errorMessage = "Some data was incorrectly entered. Cannot perform action.";
        break;
      case 401 :
        errorMessage = "Data for authentication is invalid. Login failed.";
        break;
      case 403 || 405:
        errorMessage = "The action cannot be performed for lack of rights or for any other reason.";
        break;
      case 404 :
        errorMessage = "Requested data not found.";
        break;
      case 406 :
        errorMessage = "The server can only generate a response that is not accepted by the client.";
        break;
      case 407 :
        errorMessage = "You must first authenticate itself with the proxy.";
        break;
      case 408 :
        errorMessage = "The server timed out waiting for the request.";
        break;
      case 409 :
        errorMessage = "The request could not be completed because of a conflict in the request.";
        break;
      case 410 :
        errorMessage = "The requested page is no longer available.";
        break;
      case 411 :
        errorMessage = "The \"Content-Length\" is not defined. The server will not accept the request without it .";
        break;
      case 412 :
        errorMessage = "The precondition given in the request evaluated to false by the server.";
        break;
      case 413 :
        errorMessage = "The server will not accept the request, because the request entity is too large.";
        break;
      case 414 :
        errorMessage = "The server will not accept the request, because the URI is too long. " +
          "Occurs when you convert a POST request to a GET request with a long query information .";
        break;
      case 415 :
        errorMessage = "The server will not accept the request, because the media type is not supported .";
        break;
      case 416 :
        errorMessage = "The client has asked for a portion of the file, but the server cannot supply that portion.";
        break;
      case 417 :
        errorMessage = "The server cannot meet the requirements of the Expect request-header field.";
        break;
      case 500 :
        errorMessage = "There was an error on the server side, please try it later.";
        break;
      case 501 :
        errorMessage = "The server either does not recognize the request method," +
          " or it lacks the ability to fulfill the request.";
        break;
      case 502 :
        errorMessage = "The server was acting as a gateway or proxy and " +
          "received an invalid response from the upstream server.";
        break;
      case 503 :
        errorMessage = "The server is currently unavailable (overloaded or down).";
        break;
      case 504 :
        errorMessage = "The server was acting as a gateway or proxy and did not" +
          " receive a timely response from the upstream server.";
        break;
      case 505 :
        errorMessage = "The server does not support the HTTP protocol version used in the request.";
        break;
      case 511 :
        errorMessage = "The client needs to authenticate to gain network access.";
        break;
      default :
        errorMessage = "Unknown error. Please, try few later."
    }

    this.error$.next(errorMessage);
  }

  clear(){
    this.error$.next('');
  }
}
