import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class I18nService extends HttpService {

  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  translate(elements: any, isoCode: string): Observable<any> {
    return this.post<any>('api/translations/i18n', elements, isoCode);
  }
}
