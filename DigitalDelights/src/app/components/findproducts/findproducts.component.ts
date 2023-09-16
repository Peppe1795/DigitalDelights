import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductsService } from 'src/app/services/products.service';
import { Product } from 'src/app/models/products';

@Component({
  selector: 'app-findproducts',
  templateUrl: './findproducts.component.html',
  styleUrls: ['./findproducts.component.scss'],
})
export class FindproductsComponent implements OnInit {
  products: Product[] = [];
  searchQuery: string = '';

  constructor(
    private route: ActivatedRoute,
    private productSrv: ProductsService
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      this.searchQuery = params['q'];
      this.productSrv.searchProducts(this.searchQuery).subscribe((products) => {
        this.products = products;
      });
    });
  }
}
