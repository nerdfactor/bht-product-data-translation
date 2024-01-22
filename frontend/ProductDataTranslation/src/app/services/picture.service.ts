import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Picture } from '../models/picture';
import { Product } from '../models/product';

@Injectable({
  providedIn: 'root'
})
export class PictureService extends HttpService {

  private pictureUrl = 'api/pictures';
  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  upload(image: any, product: Product): Observable<HttpEvent<Picture>> {
    const formData: FormData = new FormData();
    formData.append('file', image);
    const request = new HttpRequest('POST', `${this.url}${this.pictureUrl}?productId=${product.id}`, formData, {
      responseType: 'json'
    });
    return this.httpClient.request<Picture>(request);
  }

  update(picture: Picture, image: any) {
    const formData: FormData = new FormData();
    formData.append('file', image);
    return this.httpClient.put<Picture>(this.url + this.pictureUrl + '/' + picture.id, formData, {
      responseType: 'json'
    });
  }

  download(id: number): Observable<Blob> {
    return this.httpClient.get<any>(this.getPictureUrl(id), {
      headers: { Accept: 'blob' },
      responseType: 'blob' as 'json'
    });
  }

  remove(picture: Picture) {
    return this.httpClient.delete<any>(this.url + this.pictureUrl + '/' + picture.id);
  }

  getPictureUrl(id: number): string {
    return this.url + this.pictureUrl + '/' + id;
  }
}
