import {Component, OnInit, ViewChild} from '@angular/core';
import {ProductService} from '../../services/product.service';
import {Product} from '../../models/product';
import {debounceTime, distinctUntilChanged, Observable, of, Subject} from 'rxjs';
import {I18nService} from '../../services/i18n.service';
import {LanguageService} from '../../services/language.service';
import {Language} from '../../models/language';
import {MatPaginator, PageEvent} from '@angular/material/paginator';
import {MatSort, Sort} from '@angular/material/sort';

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrl: './search-page.component.scss'
})
export class SearchPageComponent implements OnInit {

  displayedColumns: string[] = ['id', 'name', 'description', 'action'];
  displayedProducts$!: Observable<Product[]>;
  elements: any;
  currentLanguage!: Language;
  searchTerm$: Subject<string> = new Subject<string>();
  currentSearch: string = '';
  totalProducts: number = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;


  constructor(private productService: ProductService, private languageService: LanguageService, private i18nService: I18nService) {
  }

  ngOnInit(): void {
    let elements = {
      id: 'ID',
      name: 'Name',
      description: 'Beschreibung',
      search: 'Suchen',
      itemsPerPageLabel: 'Produkte pro Seite',
      nextPageLabel: 'Nächste Seite',
      previousPageLabel: 'Vorherige Seite',
      rangeOf: 'Insgesamt'
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
      this.onSetProductSearch(value)
    });
  }

  init(elements: any, language: Language): void {
    this.currentLanguage = language;
    this.i18nService.translate(elements, language.isoCode).subscribe(elements => {
      this.elements = elements;
      this.onPaginatorI18n(elements);
    });
    this.displayedProducts$ = of([]);
    this.onSetProductSearch('')
  }

  /**
   * Set the product search to the given search term.
   * @param search The search term to set.
   */
  onSetProductSearch(search: string) {
    this.currentSearch = search;
    this.onProductSearch(search, 0, this.paginator?.pageSize || 10, this.sort?.active || 'id', this.sort?.direction || 'asc');
  }

  /**
   * Search for products with the given search term and parameters.
   * @param search The search term to search for.
   * @param page The page to display.
   * @param size The number of elements to display per page.
   * @param sort The sort column.
   * @param direction The sort direction.
   */
  onProductSearch(search: string = '', page: number = 0, size: number = 10, sort: string = 'id', direction: string = 'asc'): void {
    this.productService.searchProducts(search, this.currentLanguage?.isoCode, page, size, sort, direction).subscribe(response => {
      this.displayedProducts$ = of(response.content);
      this.totalProducts = response.totalElements;
      this.paginator.length = response.totalElements;
      this.paginator.pageIndex = response.number;
      this.paginator.pageSize = response.size;
    });
  }

  /**
   * Called when the sort direction or column changes.
   * The product search is called with the new sort parameters.
   * @param event The sort event.
   */
  onSortChanged(event: Sort) {
    this.onProductSearch(this.currentSearch, 0, this.paginator.pageSize, event.active, event.direction);
  }

  /**
   * Called when the page changes.
   * The product search is called with the new page parameters.
   * @param event The page event.
   */
  onPageChanged(event: PageEvent) {
    this.onProductSearch('', event.pageIndex, event.pageSize, this.sort.active, this.sort.direction);
  }

  /**
   * Set the paginator elements to the given translations.
   * @param elements The translated paginator elements.
   */
  onPaginatorI18n(elements: any) {
    this.paginator._intl.itemsPerPageLabel = elements.itemsPerPageLabel;
    this.paginator._intl.nextPageLabel = elements.nextPageLabel;
    this.paginator._intl.previousPageLabel = elements.previousPageLabel;
    this.paginator._intl.getRangeLabel = (page: number, pageSize: number, length: number) => {
      const start = page * pageSize + 1;
      const end = Math.min((page + 1) * pageSize, length);
      return `${start} - ${end} (${elements.rangeOf} ${length})`;
    };
  }
}
