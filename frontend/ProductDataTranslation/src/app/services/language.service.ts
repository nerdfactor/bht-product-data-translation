import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { HttpClient } from '@angular/common/http';
import { Language } from '../models/language';
import { Observable, Subject, first, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LanguageService extends HttpService {

  languageUrl: string = 'api/languages';
  currentLanguage?: Language;
  defaultLanguage?: Language;

  onLanguageChanged: Subject<Language> = new Subject<Language>();

  constructor(httpClient: HttpClient) {
    super(httpClient);
   }

   getLanguages(): Observable<Language[]> {
    return this.get<Language[]>(this.languageUrl).pipe(
      tap(languages => {
          this.defaultLanguage = languages.find(language => language.isoCode == 'de');
      }));
   }

   changeLanguage(language: Language) {
    this.currentLanguage = language;
    this.onLanguageChanged.next(this.currentLanguage);
   }
}
