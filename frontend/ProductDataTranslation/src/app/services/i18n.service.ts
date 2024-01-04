import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class I18nService extends HttpService {

  constructor(httpClient: HttpClient) {
    super(httpClient)
  }

  translate(elements: Object): Observable<Object> {
    return this.httpClient.post<Object>(this.url + 'api/translations/i18n', elements);
  }
}
