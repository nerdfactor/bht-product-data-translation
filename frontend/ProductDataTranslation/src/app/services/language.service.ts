import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { HttpClient } from '@angular/common/http';
import { Language } from '../models/language';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LanguageService extends HttpService {

  languageUrl: string = 'api/languages';
  constructor(httpClient: HttpClient) {
    super(httpClient);
   }

   getLanguages(): Observable<Language[]> {
    return this.httpClient.get<Language[]>(this.url + this.languageUrl);
   }
}
