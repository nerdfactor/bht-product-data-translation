import { Component, ElementRef, ViewChild } from '@angular/core';
import { Product } from '../../models/product';
import { FormBuilder, Validators } from '@angular/forms';
import { ProductService } from '../../services/product.service';
import { ActivatedRoute } from '@angular/router';
import { Observable, first } from 'rxjs';
import { LanguageService } from '../../services/language.service';
import { Language } from '../../models/language';
import { I18nService } from '../../services/i18n.service';
import { PictureService } from '../../services/picture.service';

@Component({
  selector: 'app-edit-page',
  templateUrl: './edit-page.component.html',
  styleUrl: './edit-page.component.scss'
})
export class EditPageComponent {

  @ViewChild('picInput')
  picInput!: ElementRef;

  product!: Product;
  elements$!: Observable<any>;
  currentLanguage?: Language;
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
  imagePreview: any;
  imageChanged: boolean = false;
  image: any;

  constructor(private route: ActivatedRoute,
    private productService: ProductService,
    private languageService: LanguageService,
    private i18nService: I18nService,
    private pictureService: PictureService,
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
    this.currentLanguage = language;
    this.elements$ = this.i18nService.translate(elements, language.isoCode);
    this.productService.getProduct(productId, language.isoCode).pipe(first()).subscribe(product => {
      this.product = product;
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

  onSubmit() {
    if (this.imageChanged) {
      if (this.product.pictures?.length > 0)
        this.pictureService.update(this.product.pictures[0], this.image).subscribe(picture => this.product.pictures = [picture]);
      else
        this.pictureService.upload(this.image, this.product).subscribe((response: any) => this.product.pictures = [response.body]);
      this.imageChanged = false;
    }

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
