import {Injectable} from '@angular/core';
import {Product} from '../models/product';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {HttpService} from './http.service';
import {Page} from '../models/page';

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

  /**
   * Search for products by name or description in a specific language and add a sorting and pagination parameters.
   * @param search
   * @param isoCode
   * @param page
   * @param size
   * @param sort
   * @param direction
   */
  searchProducts(search: string, isoCode: string, page: number = 0, size: number = 10, sort: string = 'id', direction: string = 'asc'): Observable<Page<Product>> {
    let url = this.productsUri + `/search?page=${page}&size=${size}&sort=${sort},${direction}&query=${search}`;
    return this.get<Page<Product>>(url, isoCode);
  }

  getProduct(id: number, isoCode: string): Observable<Product> {
    return this.get<Product>(this.productsUri + '/' + id, isoCode);
  }

  createProduct(product: Product): Observable<Product> {
    return this.post<Product>(this.productsUri, product, 'de');
  }

  updateProduct(product: Product, isoCode: string): Observable<Product> {
    return this.put<Product>(this.productsUri + '/' + product.id, product, isoCode);
  }

  deleteProduct(product: Product): Observable<any> {
    return this.delete(this.productsUri + '/' + product.id);
  }
}
