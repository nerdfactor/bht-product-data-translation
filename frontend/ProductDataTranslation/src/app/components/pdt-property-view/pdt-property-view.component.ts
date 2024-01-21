import { Component, Input } from '@angular/core';
import { Product } from '../../models/product';
import { Language } from '../../models/language';

@Component({
  selector: 'pdt-property-view',
  templateUrl: './pdt-property-view.component.html',
  styleUrl: './pdt-property-view.component.scss'
})
export class PdtPropertyViewComponent {
  @Input()
  elements: any

  @Input()
  product?: Product

  @Input()
  language?: Language
}
