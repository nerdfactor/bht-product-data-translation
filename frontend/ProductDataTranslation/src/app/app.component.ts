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

  languages!: Language[];
  constructor(public languageService: LanguageService) { }

  ngOnInit(): void {
    let locale = AppComponent.getUsersLocale('de_DE');
    this.languageService.getLanguages()
      .subscribe(languages => {
        this.languageService.changeLanguage(languages.find(language => language.isoCode.startsWith(locale))!);
        this.languages = languages;
      });
  }

  onLanguageSwitch(language: Language): void {
    this.languageService.changeLanguage(language);
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
