import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { HttpClient, HttpEvent, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Picture } from '../models/picture';

@Injectable({
  providedIn: 'root'
})
export class PictureService extends HttpService {

  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  upload(picture: any): Observable<HttpEvent<Picture>> {
    const formData: FormData = new FormData();
    formData.append('file', picture);
    const request = new HttpRequest('POST', this.url + 'api/pictures', formData, {
      responseType: 'json'
    });
    return this.httpClient.request<Picture>(request);
    //return this.post<any>('api/pictures', picture, isoCode);
  }

  download(id: number): Observable<any> {
    return this.httpClient.get<any>('api/pictures' + id);
  }
}
