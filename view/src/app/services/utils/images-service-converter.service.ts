import { Injectable } from '@angular/core';
import {Observable, Observer} from "rxjs";

// in bytes, compress images larger than 1MB
const fileSizeMax = 1024 * 1024
// in pixels, compress images have the width or height larger than 1024px
const widthHeightMax = 700
const defaultWidthHeightRatio = 1
const defaultQualityRatio = 0.7
@Injectable({
  providedIn: 'root'
})
export class ImagesServiceConverter {

  constructor() { }

  public blobToFile = (theBlob: Blob, fileName:string): File => {
    var b: any = theBlob;
    b.lastModifiedDate = new Date();
    b.name = fileName;

    return <File>theBlob;
  }

  compress(file: File): Observable<File> {
    const imageType = file.type || 'image/jpeg'
    const reader = new FileReader()
    reader.readAsDataURL(file)

    // @ts-ignore
    return Observable.create(observer => {
      // This event is triggered each time the reading operation is successfully completed.
      reader.onload = ev => {
        // Create an html image element
        const img = this.createImage(ev)
        // Choose the side (width or height) that longer than the other
        const imgWH = img.width > img.height ? img.width : img.height

        // Determines the ratios to compress the image
        let withHeightRatio = (imgWH > widthHeightMax) ? widthHeightMax/imgWH : defaultWidthHeightRatio
        let qualityRatio = (file.size > fileSizeMax) ? fileSizeMax/file.size : defaultQualityRatio

        // Fires immediately after the browser loads the object
        img.onload = () => {
          const elem = document.createElement('canvas')
          // resize width, height
          elem.width = img.width * withHeightRatio
          elem.height = img.height * withHeightRatio

          const ctx = <CanvasRenderingContext2D>elem.getContext('2d')
          ctx.drawImage(img, 0, 0, elem.width, elem.height)
          ctx.canvas.toBlob(
            // callback, called when blob created
            blob => {
              observer.next(new File(
                [blob as Blob],
                file.name,
                {
                  type: imageType,
                  lastModified: Date.now(),
                }
              ))
            },
            imageType,
            qualityRatio, // reduce image quantity
          )
        }
      }

      // Catch errors when reading file
      reader.onerror = error => observer.error(error)
    })
  }

  private createImage(ev: any) {
    let imageContent = ev.target.result
    const img = new Image()
    img.src = imageContent
    return img
  }

  dataURItoBlob(dataURI: string): Observable<Blob> {
    return Observable.create((observer: Observer<Blob>) => {
      const byteString: string = window.atob(dataURI);
      const arrayBuffer: ArrayBuffer = new ArrayBuffer(byteString.length);
      const int8Array: Uint8Array = new Uint8Array(arrayBuffer);
      for (let i = 0; i < byteString.length; i++) {
        int8Array[i] = byteString.charCodeAt(i);
      }
      const blob = new Blob([int8Array], { type: "image/jpeg" });
      observer.next(blob);
      observer.complete();
    });
  }
}
