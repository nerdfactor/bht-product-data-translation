import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { Observable, map, of } from 'rxjs';
import { AppComponent } from '../../app.component';
import { I18nService } from '../../services/i18n.service';
import { LanguageService } from '../../services/language.service';
import { Language } from '../../models/language';

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrl: './search-page.component.scss'
})
export class SearchPageComponent implements OnInit {

  displayedColumns: string[] = ['id', 'name', 'description', 'action'];
  products$!: Observable<Product[]>;
  displayedProducts$!: Observable<Product[]>;
  elements$!: Observable<any>;
  currentLanguage!: Language;
  search: string = '';

  constructor(private productService: ProductService, private languageService: LanguageService, private i18nService: I18nService) { }

  ngOnInit(): void {
    let elements = {
      id: 'ID',
      name: 'Name',
      description: 'Beschreibung',
      search: 'Suchen'
    };

    this.languageService.onLanguageChanged.subscribe(language => this.init(elements, language));
    if (this.languageService.currentLanguage)
      this.init(elements, this.languageService.currentLanguage);
  }

  init(elements: any, language: Language): void {
    this.currentLanguage = language;
    this.elements$ = this.i18nService.translate(elements, language.isoCode);
    this.products$ = this.productService.getProducts(language.isoCode);
    this.onProductSearch();
  }

  onProductSearch(): void {
    this.displayedProducts$ = this.products$.pipe(map(products => products.filter(product => product.name.toLowerCase().startsWith(this.search.toLowerCase()))))
  }
}
