import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { Observable, first } from 'rxjs';
import { I18nService } from '../../services/i18n.service';
import { LanguageService } from '../../services/language.service';
import { Language } from '../../models/language';
import { PictureService } from '../../services/picture.service';

@Component({
  selector: 'app-detail-page',
  templateUrl: './detail-page.component.html',
  styleUrl: './detail-page.component.scss'
})
export class DetailPageComponent implements OnInit {

  product!: Product;
  elements$?: Observable<any>;
  currentLanguage?: Language;
  image: any;

  constructor(private route: ActivatedRoute,
    private productService: ProductService,
    private languageService: LanguageService,
    private pictureService: PictureService,
    private i18nService: I18nService) { }

  ngOnInit(): void {
    const productId = Number(this.route.snapshot.paramMap.get('id'));
    const elements = {
      serial: 'Seriennummer',
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

    this.languageService.onLanguageChanged.subscribe(language => this.init(elements,  productId, language));
    if (this.languageService.currentLanguage)
      this.init(elements, productId, this.languageService.currentLanguage);
  }

  init(elements: any, productId: number, language: Language): void {
    this.currentLanguage = language;
    this.elements$ = this.i18nService.translate(elements, language.isoCode);
    this.productService.getProduct(productId, language.isoCode).pipe(first()).subscribe(product => {
      this.product = product;
      if (product.pictures?.length > 0)
        this.image = this.pictureService.getPictureUrl(product.pictures[0].id)
    });
  }
}
