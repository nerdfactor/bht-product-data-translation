import { Component, OnInit, ElementRef, ViewChild } from '@angular/core';
import { Product } from '../../models/product';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { ActivatedRoute, Navigation, Router } from '@angular/router';
import { Observable, catchError, first, of, tap } from 'rxjs';
import { LanguageService } from '../../services/language.service';
import { Language } from '../../models/language';
import { I18nService } from '../../services/i18n.service';
import { PictureService } from '../../services/picture.service';
import { ColorService } from '../../services/color.service';
import { CategoryService } from '../../services/category.service';
import { Color } from '../../models/color';
import { Category } from '../../models/category';
import { TranslationService } from '../../services/translation.service';
import { Translation } from '../../models/translation';
import { MatDialog } from '@angular/material/dialog';
import { PdtDeletionConfirmationComponent } from '../pdt-deletion-confirmation/pdt-deletion-confirmation.component';

@Component({
  selector: 'app-edit-page',
  templateUrl: './edit-page.component.html',
  styleUrl: './edit-page.component.scss'
})
export class EditPageComponent implements OnInit {

  @ViewChild('picInput')
  picInput!: ElementRef;

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
  imagePreview: any;
  imageChanged: boolean = false;
  image: any;

  constructor(private route: ActivatedRoute,
    private productService: ProductService,
    public languageService: LanguageService,
    private i18nService: I18nService,
    private pictureService: PictureService,
    private colorService: ColorService,
    private categoryService: CategoryService,
    private translationService: TranslationService,
    private router: Router,
    private dialog: MatDialog,
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
      const translation = this.product.translations.find(translation => translation.language!.id == language.id);
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

      if (product.pictures?.length > 0) {
        this.imagePreview = this.pictureService.getPictureUrl(product.pictures[0].id);
      }
    });
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
    if (this.product.pictures?.length > 0) {
      this.pictureService.remove(this.product.pictures[0]).subscribe();
      this.product.pictures = [];
    }
  }

  deleteProduct() {
    const delConfirmDialog = this.dialog.open(PdtDeletionConfirmationComponent);
    delConfirmDialog.afterClosed().subscribe(result => {
      if (result) {
        this.productService.deleteProduct(this.product);
        this.router.navigate(['/search']);
      }
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
    const translation = this.product.translations.find(translation => translation.language!.id == this.currentLanguage!.id)!;
    translation.shortDescription = formValue.shortDescription!;
    translation.longDescription = formValue.longDescription!;

    if (this.imageChanged) {
      if (this.product.pictures?.length > 0) {
        this.pictureService.update(this.product.pictures[0], this.image).subscribe(picture => {
          this.product.pictures = [picture];
          this.update(this.product, translation);
        });
      }
      else {
        this.pictureService.upload(this.image, this.product).subscribe((response: any) => {
          this.product.pictures = [response.body];
          this.update(this.product, translation);
        });
      }
      this.imageChanged = false;
    }
    else
      this.update(this.product, translation);

    console.log('submitting', this.product);
  }
  
  update(product: Product, translation: Translation) {
    if (this.currentLanguage?.isoCode == this.languageService.defaultLanguage?.isoCode)
      this.productService.updateProduct(product, this.currentLanguage!.isoCode).subscribe();
    else {
      translation.product = {
        id: product.id,
        categories: [],
        colors: [],
        depth: product.depth,
        height: product.height,
        name: product.name,
        pictures: [],
        price: product.price,
        serial: product.serial,
        translations: [],
        weight: product.weight, width: product.width
      };
      this.translationService.updateTranslation(translation, this.currentLanguage!.isoCode).subscribe();
    }
  }
}
