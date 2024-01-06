import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { Observable, of } from 'rxjs';
import { I18nService } from '../../services/i18n.service';
import { LanguageService } from '../../services/language.service';
import { Language } from '../../models/language';

@Component({
  selector: 'app-detail-page',
  templateUrl: './detail-page.component.html',
  styleUrl: './detail-page.component.scss'
})
export class DetailPageComponent implements OnInit {

  product$?: Observable<Product>;
  elements$?: Observable<any>;
  currentLanguage?: Language;

  constructor(private route: ActivatedRoute, private productService: ProductService, private languageService: LanguageService, private i18nService: I18nService) { }

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
      photo: 'Foto'
    };

    this.languageService.onLanguageChanged.subscribe(language => {
      this.currentLanguage = language;
      this.elements$ = this.i18nService.translate(elements, language.isoCode);
      this.product$ = this.productService.getProduct(productId, language.isoCode);
    });

    this.elements$ = of(elements);
  }
}
