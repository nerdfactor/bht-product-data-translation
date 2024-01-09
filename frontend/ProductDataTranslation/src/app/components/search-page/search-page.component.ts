import {Component, OnInit, ViewChild} from '@angular/core';
import {ProductService} from '../../services/product.service';
import {Product} from '../../models/product';
import {debounceTime, distinctUntilChanged, Observable, of, Subject} from 'rxjs';
import {I18nService} from '../../services/i18n.service';
import {LanguageService} from '../../services/language.service';
import {Language} from '../../models/language';
import {MatPaginator, PageEvent} from "@angular/material/paginator";

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
  totalProducts: number = 0

  @ViewChild(MatPaginator) paginator!: MatPaginator;


  constructor(private productService: ProductService, private languageService: LanguageService, private i18nService: I18nService) {
  }

  ngOnInit(): void {
    let elements = {
      id: 'ID',
      name: 'Name',
      description: 'Beschreibung',
      search: 'Suchen',
      itemsPerPageLabel: "Produkte pro Seite",
      nextPageLabel: "Nächste Seite",
      previousPageLabel: "Vorherige Seite",
      rangeOf: "Insgesamt"
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
      this.onProductSearch(value, 0)
    })
  }

  init(elements: any, language: Language): void {
    this.currentLanguage = language;
    this.elements$ = this.i18nService.translate(elements, language.isoCode);
    this.elements$.subscribe(value => {
      this.onPaginatorI18n(value)
    })
    this.products$ = this.productService.getProducts(language.isoCode);
    this.onProductSearch("");
    //this.onPaginatorI18n(elements)
  }

  onProductSearch(search: string = "", page: number = 0, size: number = 10, sort: string = "id", direction: string = "asc"): void {
    this.productService.searchProducts(search, this.currentLanguage?.isoCode, page, size, sort, direction).subscribe(response => {
      this.displayedProducts$ = of(response.content);
      this.totalProducts = response.totalElements
      this.paginator.length = response.totalElements
    });
  }

  onPageChanged(event: PageEvent) {
    this.onProductSearch("", event.pageIndex, event.pageSize)
  }

  onPaginatorI18n(elements: any) {
    this.paginator._intl.itemsPerPageLabel = elements.itemsPerPageLabel
    this.paginator._intl.nextPageLabel = elements.nextPageLabel
    this.paginator._intl.previousPageLabel = elements.previousPageLabel
    this.paginator._intl.getRangeLabel = (page: number, pageSize: number, length: number) => {
      const start = page * pageSize + 1;
      const end = (page + 1) * pageSize;
      return `${start} - ${end} (${elements.rangeOf} ${length})`;
    };
  }
}
