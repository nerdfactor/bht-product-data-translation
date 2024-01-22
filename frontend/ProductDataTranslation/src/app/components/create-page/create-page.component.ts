import { Component, OnDestroy, OnInit } from '@angular/core';
import { I18nService } from '../../services/i18n.service';
import { LanguageService } from '../../services/language.service';
import { ProductService } from '../../services/product.service';
import { FormBuilder, Validators } from '@angular/forms';
import { Language } from '../../models/language';
import { Observable, Subscription, mergeWith, of } from 'rxjs';
import { Product } from '../../models/product';
import { Color } from '../../models/color';
import { ColorService } from '../../services/color.service';
import { Category } from '../../models/category';
import { CategoryService } from '../../services/category.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PictureService } from '../../services/picture.service';

@Component({
  selector: 'app-create-page',
  templateUrl: './create-page.component.html',
  styleUrl: './create-page.component.scss'
})
export class CreatePageComponent implements OnInit, OnDestroy {

  currentLanguage?: Language;
  elements!: any;
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
  colors$?: Observable<Color[]>;
  categories$?: Observable<Category[]>;
  productColors: Color[] = [];
  productCategories: Category[] = [];
  imagePreview: any;
  imageChanged: boolean = false;
  image: any;

  constructor(private productService: ProductService,
    private languageService: LanguageService,
    private i18nService: I18nService,
    private colorService: ColorService,
    private categoryService: CategoryService,
    private pictureService: PictureService,
    private snackBar: MatSnackBar,
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
      warning: 'Alle Eingaben müssen auf Deutsch erfolgen.',
      saved: 'Gespeichert',
      close: 'Schließen'
    };

    this.languageChangedSubscription = this.languageService.onLanguageChanged.subscribe(language => this.init(elements, language));

    if (this.languageService.currentLanguage)
      this.init(elements, this.languageService.currentLanguage);
  }

  init(elements: any, language: Language) {
    this.currentLanguage = this.languageService.currentLanguage;
    this.i18nService.translate(elements, language.isoCode).subscribe(elements => this.elements = elements);
    this.colors$ = this.colorService.getColors(language.isoCode);
    this.categories$ = this.categoryService.getCategories(language.isoCode);
  }

  selectPicture(event: any) {
    let selectedFiles = event.target.files;
    if (!selectedFiles) 
      return;

    const picture = selectedFiles.item(0);
    this.imagePreview = URL.createObjectURL(picture);
    this.image = picture;
    this.imageChanged = true;
  }

  deletePicture() {
    this.imagePreview = undefined;
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
      translations: []
    };

    this.productService.createProduct(product).subscribe(p => {
      p.categories = this.productCategories;
      p.colors = this.productColors;
      p.translations = [
        {
          id: 0,
          longDescription: formValue.longDescription!,
          shortDescription: formValue.shortDescription!,
          language: this.languageService.defaultLanguage
        }
      ]
      if (this.imageChanged) {
        this.pictureService.upload(this.image, p).subscribe((response: any) => {
          p.pictures = [response.body];
          this.updateProduct(p);
        });
      }
      else {
        this.updateProduct(p);
      }
    });
  }

  updateProduct(product: Product) {
    this.productService.updateProduct(product, this.currentLanguage!.isoCode).subscribe(prod => {
      this.productForm.reset();
      this.colors$ = this.colors$?.pipe(mergeWith(of(prod.colors)));
      this.categories$ = this.categories$?.pipe(mergeWith(of(prod.categories)));
      this.productCategories = [];
      this.productColors = [];
      this.imagePreview = undefined;
      this.imageChanged = false;
      this.image = undefined;
      this.snackBar.open(this.elements['saved'], this.elements['close'], {
        duration: 3000
      });
    });
  }

  ngOnDestroy(): void {
    this.languageChangedSubscription?.unsubscribe();
  }
}
