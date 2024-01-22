import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {HttpService} from './http.service';
import { Translation } from '../models/translation';

@Injectable({
  providedIn: 'root'
})
export class TranslationService extends HttpService {

  productsUri: string = 'api/translations';

  constructor(httpClient: HttpClient) {
    super(httpClient);
  }

  updateTranslation(translation: Translation, isoCode: string): Observable<Translation> {
    return this.put<Translation>(this.productsUri + "/" + translation.id, translation, isoCode);
  }
}