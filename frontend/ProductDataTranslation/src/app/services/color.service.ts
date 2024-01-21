import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {HttpService} from './http.service';
import {Color} from '../models/color';

@Injectable({
  providedIn: 'root'
})
export class ColorService extends HttpService {

  productsUri: string = 'api/colors';

  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  getColors(isoCode: string): Observable<Color[]> {
    return this.get<Color[]>(this.productsUri, isoCode);
  }

  createColor(product: Color): Observable<Color> {
    return this.post<Color>(this.productsUri, product, 'de');
  }
}
