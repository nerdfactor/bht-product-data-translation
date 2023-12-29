import { Component, OnInit } from '@angular/core';
import { LanguageService } from './services/language.service';
import { Observable, mergeMap, first } from 'rxjs';
import { Language } from './models/language';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {

  languages$!: Observable<Language[]>
  currentLanguage?: Language;
  constructor(private languageService: LanguageService) { }

  ngOnInit(): void {
    this.languages$ = this.languageService.getLanguages();
    let locale = AppComponent.getUsersLocale('de_DE');
    this.languages$.pipe(mergeMap(languages => languages), first(language => language.isoCode.startsWith(locale)))
      .subscribe(language => this.currentLanguage = language);
  }

  onLanguageSwitch(language: Language): void {
    this.currentLanguage = language;
  }

  static getUsersLocale(defaultValue: string): string {
    if (typeof window === 'undefined' || typeof window.navigator === 'undefined') {
      return defaultValue;
    }
    const wn = window.navigator as any;
    let lang = wn.languages ? wn.languages[0] : defaultValue;
    lang = lang || wn.language || wn.browserLanguage || wn.userLanguage;
    return lang;
  }
}
