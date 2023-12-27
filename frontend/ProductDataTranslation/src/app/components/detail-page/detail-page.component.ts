import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product } from '../../models/product';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-detail-page',
  templateUrl: './detail-page.component.html',
  styleUrl: './detail-page.component.scss'
})
export class DetailPageComponent implements OnInit {

  product$?: Observable<Product>;
  constructor(private route: ActivatedRoute, private productService: ProductService) { }

  ngOnInit(): void {
    const productId = Number(this.route.snapshot.paramMap.get('id'));
    this.product$ = this.productService.getProduct(productId)
  }
}
