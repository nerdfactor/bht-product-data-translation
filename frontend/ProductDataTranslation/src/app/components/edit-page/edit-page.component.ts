import { Component } from '@angular/core';
import { Product } from '../../models/product';
import { FormBuilder, Validators } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { ActivatedRoute } from '@angular/router';
import { first, tap } from 'rxjs';

@Component({
  selector: 'app-edit-page',
  templateUrl: './edit-page.component.html',
  styleUrl: './edit-page.component.scss'
})
export class EditPageComponent {

  product!: Product;
  constructor(private route: ActivatedRoute, private productService: ProductService, private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    const productId = Number(this.route.snapshot.paramMap.get('id'));
    this.productService.getProduct(productId, 'de').pipe(
      first(),
      tap(product => {
        this.productForm.patchValue({
          name: product.name,
          depth: product.depth,
          height: product.height,
          price: product.price,
          serial: product.serial,
          weight: product.weight,
          width: product.width,
        })
      }),
      ).subscribe(product => this.product = product);
  }

  productForm = this.formBuilder.group({
    name: ['', Validators.required],
    depth: [0],
    height: [0],
    price: [0],
    serial: ['', Validators.required],
    weight: [0],
    width: [0],
    shortDescription: [''],
    longDescription: ['']
  })

  onSubmit() {
    const formValue = this.productForm.value;
    this.product.name = formValue.name!;
    this.product.depth = formValue.depth!;
    this.product.height = formValue.height!;
    this.product.price = formValue.price!;
    this.product.serial = formValue.serial!;
    this.product.weight = formValue.weight!;
    this.product.width = formValue.width!;
    console.log('submitting', this.product);
  }
}
