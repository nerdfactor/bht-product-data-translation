import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { HttpClient } from '@angular/common/http';
import { Language } from '../models/language';
import { Observable, Subject, first } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LanguageService extends HttpService {

  languageUrl: string = 'api/languages';
  currentLanguage?: Language;

  onLanguageChanged: Subject<Language> = new Subject<Language>();

  constructor(httpClient: HttpClient) {
    super(httpClient);
   }

   getLanguages(): Observable<Language[]> {
    return this.get<Language[]>(this.languageUrl);
   }

   changeLanguage(language: Language) {
    this.currentLanguage = language;
    this.onLanguageChanged.next(this.currentLanguage);
   }
}
