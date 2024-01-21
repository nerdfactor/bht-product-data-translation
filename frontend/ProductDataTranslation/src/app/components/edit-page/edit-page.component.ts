import { Component, OnInit } from '@angular/core';
import { Product } from '../../models/product';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { ActivatedRoute } from '@angular/router';
import { Observable, catchError, first, of, tap } from 'rxjs';
import { LanguageService } from '../../services/language.service';
import { Language } from '../../models/language';
import { I18nService } from '../../services/i18n.service';
import { ColorService } from '../../services/color.service';
import { CategoryService } from '../../services/category.service';
import { Color } from '../../models/color';
import { Category } from '../../models/category';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-edit-page',
  templateUrl: './edit-page.component.html',
  styleUrl: './edit-page.component.scss'
})
export class EditPageComponent implements OnInit {

  product!: Product;
  elements$!: Observable<any>;
  currentLanguage?: Language;
  colors$?: Observable<Color[]>;
  categories$?: Observable<Category[]>;
  colorControl = new FormControl('');
  productForm = this.formBuilder.group({
    name: ['', Validators.required],
    depth: [0],
    height: [0],
    price: [0],
    serial: ['', Validators.required],
    weight: [0],
    width: [0],
    shortDescription: [''],
    longDescription: [''],
    categories: this.formBuilder.group({})
  });

  constructor(private route: ActivatedRoute,
    private productService: ProductService,
    public languageService: LanguageService,
    private i18nService: I18nService,
    private colorService: ColorService,
    private categoryService: CategoryService,
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
      notLoaded: 'Produkt konnte nicht geladen werden',
    };

    this.languageService.onLanguageChanged.subscribe(language => this.init(elements, productId, language));
    if (this.languageService.currentLanguage)
      this.init(elements, productId, this.languageService.currentLanguage);
  }

  init(elements: any, productId: number, language: Language): void {
    this.currentLanguage = language;
    this.elements$ = this.i18nService.translate(elements, language.isoCode);
    this.colors$ = this.colorService.getColors(language.isoCode);
    this.categories$ = this.categoryService.getCategories(language.isoCode);
    this.productService.getProduct(productId, language.isoCode).pipe(first()).subscribe(product => {
      this.product = product;
      if (!this.product.hasOwnProperty('translations'))
        this.product.translations = [];
      if (!this.product.hasOwnProperty('categories'))
        this.product.categories = [];
      if (!this.product.hasOwnProperty('colors'))
        this.product.colors = [];
      if (!this.product.hasOwnProperty('pictures'))
        this.product.pictures = [];
      const translation = this.product.translations.find(translation => translation.language!.id == language!.id);
      this.productForm.patchValue({
        name: product.name,
        depth: product.depth,
        height: product.height,
        price: product.price,
        serial: product.serial,
        weight: product.weight,
        width: product.width,
        shortDescription: translation?.shortDescription,
        longDescription: translation?.longDescription
      });
    });
  }

  onSubmit() {
    const formValue = this.productForm.value;
    this.product.name = formValue.name!;
    this.product.serial = formValue.serial!;
    this.product.depth = formValue.depth!;
    this.product.width = formValue.width!;
    this.product.height = formValue.height!;
    this.product.weight = formValue.weight!;
    const translation = this.product.translations.find(translation => translation.language!.id = this.currentLanguage!.id)!;
    translation.shortDescription = formValue.shortDescription!;
    translation.longDescription = formValue.longDescription!;
    console.log('submitting', this.product);
    this.productService.updateProduct(this.product, this.currentLanguage!.isoCode).subscribe();
  }
}
