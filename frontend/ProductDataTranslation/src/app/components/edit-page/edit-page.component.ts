import { Component } from '@angular/core';
import { Product } from '../../models/product';
import { FormBuilder, Validators } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { ActivatedRoute } from '@angular/router';
import { Observable, first, tap } from 'rxjs';
import { LanguageService } from '../../services/language.service';
import { Language } from '../../models/language';
import { I18nService } from '../../services/i18n.service';

@Component({
  selector: 'app-edit-page',
  templateUrl: './edit-page.component.html',
  styleUrl: './edit-page.component.scss'
})
export class EditPageComponent {

  product!: Product;
  elements$!: Observable<any>;
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
  });

  constructor(private route: ActivatedRoute,
    private productService: ProductService,
    private languageService: LanguageService,
    private i18nService: I18nService,
    private formBuilder: FormBuilder) { }

  ngOnInit(): void {
    const productId = Number(this.route.snapshot.paramMap.get('id'));
    const elements = {
      name: 'Name',
      serial: 'Seriennummer',
      descriptions: 'Beschreibungen',
      shortDescription: 'Kurzbeschreibung',
      longDescription: 'Langbeschreibung',
      property: 'Eigenschaften',
      weight: 'Gewicht',
      height: 'Höhe',
      width: 'Breite',
      depth: 'Tiefe',
      price: 'Preis',
      colors: 'Farben',
      categories: 'Kategorien',
      photo: 'Foto',
      notLoaded: 'Produkt konnte nicht geladen werden'
    };

    this.languageService.onLanguageChanged.subscribe(language => this.init(elements, productId, language));
    if (this.languageService.currentLanguage)
      this.init(elements, productId, this.languageService.currentLanguage);
  }

  init(elements: any, productId: number, language: Language): void {
    this.elements$ = this.i18nService.translate(elements, language.isoCode);
    this.productService.getProduct(productId, language.isoCode).pipe(first()).subscribe(product => {
      this.product = product;
      this.productForm.patchValue({
        name: product.name,
        depth: product.depth,
        height: product.height,
        price: product.price,
        serial: product.serial,
        weight: product.weight,
        width: product.width,
      });
    });
  }

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
