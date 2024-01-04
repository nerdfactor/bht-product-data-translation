import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { Observable, map } from 'rxjs';
import { AppComponent } from '../../app.component';
import { I18nService } from '../../services/i18n.service';

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
  search: string = '';

  constructor(private productService: ProductService, private i18nService: I18nService) { }

  ngOnInit(): void {
    this.products$ = this.productService.getProducts();
    this.displayedProducts$ = this.products$;
    let elements = {
      id: 'ID',
      name: 'Name',
      description: 'Description',
      search: 'Suchen'
    };
    console.log(elements);
    this.elements$ = this.i18nService.translate(elements);
    console.log(AppComponent.getUsersLocale("de-DE"));
  }

  onProductSearch(): void {
    this.displayedProducts$ = this.products$.pipe(map(products => products.filter(product => product.name.toLowerCase().startsWith(this.search.toLowerCase()))))
  }
}
