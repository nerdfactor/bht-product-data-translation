import {Injectable} from '@angular/core';
import {Product} from '../models/product';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {HttpService} from './http.service';
import {Page} from "../models/page";

@Injectable({
  providedIn: 'root'
})
export class ProductService extends HttpService {

  productsUri: string = 'api/products';
  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  getProducts(isoCode: string): Observable<Product[]> {
    return this.get<Product[]>(this.productsUri, isoCode);
  }

  searchProducts(search: String, isoCode: string): Observable<Page<Product>> {
    return this.get<Page<Product>>(this.productsUri + '/search?query=' + search, isoCode);
  }

  getProduct(id: number, isoCode: string): Observable<Product> {
    return this.get<Product>(this.productsUri + '/' + id, isoCode);
  }

  updateProduct(product: Product, isoCode: string): Observable<Product> {
    return this.put<Product>(this.productsUri + '/' + product.id, product, isoCode);
  }
}
