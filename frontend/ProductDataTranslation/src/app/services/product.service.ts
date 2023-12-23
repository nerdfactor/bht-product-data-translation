import { Injectable } from '@angular/core';
import { Product } from '../models/product';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor() { }

  getProducts(): Observable<Product[]> {
    return of([
      { id: 1, serial: "", name: "Hammer", height: 0, width: 0, depth: 0, weight: 0, price: 0 },
      { id: 2, serial: "", name: "Sichel", height: 0, width: 0, depth: 0, weight: 0, price: 0 },
      { id: 3, serial: "", name: "Meißel", height: 0, width: 0, depth: 0, weight: 0, price: 0 },
      { id: 4, serial: "", name: "Bohrer", height: 0, width: 0, depth: 0, weight: 0, price: 0 },
      { id: 5, serial: "", name: "Dreher", height: 0, width: 0, depth: 0, weight: 0, price: 0 },
    ]);
  }
}
