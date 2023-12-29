import { Injectable } from '@angular/core';
import { Product } from '../models/product';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { HttpService } from './http.service';

@Injectable({
  providedIn: 'root'
})
export class ProductService extends HttpService {

  productsUri: string = 'api/products';
  constructor(httpClient: HttpClient)
  {
    super(httpClient);
  }

  getProducts(): Observable<Product[]> {
    return this.httpClient.get<Product[]>(this.url + this.productsUri);
  }

  getProduct(id: number): Observable<Product> {
    return this.httpClient.get<Product>(this.url + this.productsUri + '/' + id);
  }
}
