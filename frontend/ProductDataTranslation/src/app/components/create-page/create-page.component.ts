import { Component, OnDestroy, OnInit } from '@angular/core';
import { I18nService } from '../../services/i18n.service';
import { LanguageService } from '../../services/language.service';
import { ProductService } from '../../services/product.service';
import { FormBuilder, Validators } from '@angular/forms';
import { Language } from '../../models/language';
import { Observable, Subscription } from 'rxjs';
import { Product } from '../../models/product';

@Component({
  selector: 'app-create-page',
  templateUrl: './create-page.component.html',
  styleUrl: './create-page.component.scss'
})
export class CreatePageComponent implements OnInit, OnDestroy {

  currentLanguage?: Language;
  elements$!: Observable<any>;
  languageChangedSubscription?: Subscription;
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

  constructor(private productService: ProductService,
    private languageService: LanguageService,
    private i18nService: I18nService,
    private formBuilder: FormBuilder) { }
  

  ngOnInit(): void {
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
      warning: 'Alle Eingaben müssen auf Deutsch erfolgen.'
    };

    this.languageChangedSubscription = this.languageService.onLanguageChanged.subscribe(language => this.init(elements, language));

    if (this.languageService.currentLanguage)
      this.init(elements, this.languageService.currentLanguage);
  }

  init(elements: any, language: Language) {
    this.currentLanguage = this.languageService.currentLanguage;
    this.elements$ = this.i18nService.translate(elements, language.isoCode);
  }

  onSubmit() {
    const formValue = this.productForm.value;
    const product: Product = {
      id: 0,
      name: formValue.name!,
      height: formValue.height!,
      width: formValue.width!,
      depth: formValue.depth!,
      price: formValue.price!,
      serial: formValue.serial!,
      weight: formValue.weight!,
      categories: [],
      colors: [],
      pictures: [],
      translations: [
        {
          id: 0,
          longDescription: formValue.longDescription!,
          shortDescription: formValue.shortDescription!,
        }
      ]
    };
    this.productService.createProduct(product).subscribe();
  }

  ngOnDestroy(): void {
    this.languageChangedSubscription?.unsubscribe();
  }
}
