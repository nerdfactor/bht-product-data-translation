import { Component } from '@angular/core';
import { Product } from '../../models/product';

@Component({
  selector: 'app-edit-page',
  templateUrl: './edit-page.component.html',
  styleUrl: './edit-page.component.scss'
})
export class EditPageComponent {
  product: Product = {
    id: 0,
    name: '',
    categories: [],
    colors: [],
    depth: 0,
    height: 0,
    pictures: [],
    price: 0,
    serial: '0',
    translations: [],
    weight: 0,
    width: 0
  };
}
