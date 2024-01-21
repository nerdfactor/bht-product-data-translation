import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {HttpService} from './http.service';
import {Color} from '../models/color';
import {Category} from '../models/category';

@Injectable({
  providedIn: 'root'
})
export class CategoryService extends HttpService {

  productsUri: string = 'api/categories';

  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  getCategories(isoCode: string): Observable<Category[]> {
    return this.get<Color[]>(this.productsUri, isoCode);
  }

  createCategory(product: Category): Observable<Category> {
    return this.post<Color>(this.productsUri, product, 'de');
  }
}
