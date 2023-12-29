import { Component, OnInit } from '@angular/core';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { Observable, map } from 'rxjs';
import { AppComponent } from '../../app.component';

@Component({
  selector: 'app-search-page',
  templateUrl: './search-page.component.html',
  styleUrl: './search-page.component.scss'
})
export class SearchPageComponent implements OnInit {

  displayedColumns: string[] = ['id', 'name', 'description', 'action'];
  products$!: Observable<Product[]>;
  displayedProducts$!: Observable<Product[]>
  search: string = '';

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.products$ = this.productService.getProducts();
    this.displayedProducts$ = this.products$;
    console.log(AppComponent.getUsersLocale("de-DE"));
  }

  onProductSearch(): void {
    this.displayedProducts$ = this.products$.pipe(map(products => products.filter(product => product.name.toLowerCase().startsWith(this.search.toLowerCase()))))
  }
}
