import {Component, OnInit} from '@angular/core';
import {ProductService} from '../../services/product.service';
import {Product} from '../../models/product';
import {debounceTime, distinctUntilChanged, Observable, of, Subject, switchMap} from 'rxjs';
import {I18nService} from '../../services/i18n.service';
import {LanguageService} from '../../services/language.service';
import {Language} from '../../models/language';

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
  searchTerm$ = new Subject<string>();

  constructor(private productService: ProductService, private languageService: LanguageService, private i18nService: I18nService) {
  }

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

    // Debounce the reaction to changes in the search term in order
    // not to send to many requests to the backend.
    this.searchTerm$.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(value => {
      this.onProductSearch(value)
    })
  }

  init(elements: any, language: Language): void {
    this.currentLanguage = language;
    this.elements$ = this.i18nService.translate(elements, language.isoCode);
    this.products$ = this.productService.getProducts(language.isoCode);
    this.onProductSearch("");
  }

  onProductSearch(search: string): void {
    this.productService.searchProducts(search, this.currentLanguage?.isoCode).subscribe(response => {
      this.displayedProducts$ = of(response.content);
    });
  }

  onSearchChanged(event: string){
    this.onProductSearch(event)
    this.searchTerm$.next(event)
  }
}
