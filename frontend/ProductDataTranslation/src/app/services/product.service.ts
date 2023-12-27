import { Injectable } from '@angular/core';
import { Product } from '../models/product';
import { Observable, of, mergeMap, first } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor() { }

  getProducts(): Observable<Product[]> {
    return of([
      { id: 1, serial: "", name: "Hammer", height: 0, width: 0, depth: 0, weight: 0, price: 0, categories: [], colors: [], pictures: [], translations: [ {id: 0, shortDescription: "kurze Beschreibung", longDescription: "lange Zusammenfassung des Products", product: undefined, language: undefined } ] },
      { id: 2, serial: "", name: "Sichel", height: 0, width: 0, depth: 0, weight: 0, price: 0, categories: [], colors: [], pictures: [], translations: [ {id: 0, shortDescription: "kurze Beschreibung", longDescription: "lange Zusammenfassung des Products", product: undefined, language: undefined } ] },
      { id: 3, serial: "", name: "Meißel", height: 0, width: 0, depth: 0, weight: 0, price: 0, categories: [], colors: [], pictures: [], translations: [ {id: 0, shortDescription: "kurze Beschreibung", longDescription: "lange Zusammenfassung des Products", product: undefined, language: undefined } ] },
      { id: 4, serial: "", name: "Bohrer", height: 0, width: 0, depth: 0, weight: 0, price: 0, categories: [], colors: [], pictures: [], translations: [ {id: 0, shortDescription: "kurze Beschreibung", longDescription: "lange Zusammenfassung des Products", product: undefined, language: undefined } ] },
      { id: 5, serial: "", name: "Dreher", height: 0, width: 0, depth: 0, weight: 0, price: 0, categories: [], colors: [], pictures: [], translations: [ {id: 0, shortDescription: "kurze Beschreibung", longDescription: "lange Zusammenfassung des Products", product: undefined, language: undefined } ] },
    ]);
  }

  getProduct(id: number): Observable<Product> {
    return this.getProducts().pipe(
      mergeMap(products => products),
      first(product => product.id === id)
    );
  }
}
