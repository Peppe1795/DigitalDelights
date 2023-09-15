import { Component, OnInit } from '@angular/core';
import { ProductsService } from 'src/app/services/products.service';
import { Product } from 'src/app/models/products';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  products: Product[] = [];
  randomProducts: Product[] = [];

  constructor(private productService: ProductsService) {}

  ngOnInit(): void {
    this.productService.getProducts(1, 'someOrder').subscribe(
      (data) => {
        this.products = data;
        this.randomProducts = this.selectRandomProducts(this.products, 8);
      },
      (error) => {
        console.error('Errore nel recupero dei prodotti', error);
      }
    );
  }

  selectRandomProducts(products: Product[], count: number): Product[] {
    const shuffled = products.sort(() => 0.5 - Math.random());
    return shuffled.slice(0, count);
  }
}
