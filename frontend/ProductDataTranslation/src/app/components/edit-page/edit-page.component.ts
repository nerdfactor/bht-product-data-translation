import { Component } from '@angular/core';
import { Product } from '../../models/product';
import { Translation } from '../../models/translation';
import { Language } from '../../models/language';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-edit-page',
  templateUrl: './edit-page.component.html',
  styleUrl: './edit-page.component.scss'
})
export class EditPageComponent {

  constructor(private formBuilder: FormBuilder) { }

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

  language: Language = {
    id: 0,
    currency: '',
    isoCode: '',
    measure: '',
    name: '',
    translations: []
  }

  translation: Translation = {
    id: 0,
    longDescription: '',
    shortDescription: '',
    product: this.product,
    language: this.language
  }

  productForm = this.formBuilder.group({
    name: [''],
    depth: [0],
    height: [0],
    price: [0],
    serial: [''],
    weight: [0],
    width: [0],
    shortDescription: [''],
    longDescription: ['']
  })

  onSubmit() {
    this.product.name = this.productForm.value.name!;
    this.product.depth = this.productForm.value.depth!;
    this.product.height = this.productForm.value.height!;
    this.product.price = this.productForm.value.price!;
    this.product.serial = this.productForm.value.serial!;
    this.product.weight = this.productForm.value.weight!;
    this.product.width = this.productForm.value.width!;
    console.log('submitting', this.product);
  }
}
