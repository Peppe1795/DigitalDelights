import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router'; // Importiamo ActivatedRoute per accedere ai parametri dell'URL
import { Product } from 'src/app/models/products';
import { ProductsService } from 'src/app/services/products.service';
import { Category } from 'src/app/enum/category';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent implements OnInit {
  categoriesWithProducts: { [key in Category]?: Product[] } = {};
  loading = true;
  selectedCategory?: Category; // Aggiunto per gestire la categoria selezionata

  constructor(
    private productSrv: ProductsService,
    private route: ActivatedRoute // Injectiamo ActivatedRoute nel costruttore
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      if (params['category']) {
        this.selectedCategory = params['category'] as Category;
        this.loadProductsBySelectedCategory();
      } else {
        this.loadAllProductsByCategories();
      }
    });
  }

  loadAllProductsByCategories(): void {
    const categories = Object.values(Category);

    let completedRequests = 0;

    categories.forEach((category) => {
      this.productSrv.getProductsByCategory(category).subscribe(
        (response) => {
          this.categoriesWithProducts[category] = response.content;

          completedRequests++;
          if (completedRequests === categories.length) {
            this.loading = false;
          }
        },
        (error) => {
          console.error('Error fetching products by category:', error);
          this.loading = false;
        }
      );
    });
  }

  loadProductsBySelectedCategory(): void {
    if (!this.selectedCategory) return;

    this.productSrv.getProductsByCategory(this.selectedCategory).subscribe(
      (response) => {
        this.categoriesWithProducts = {};
        if (this.selectedCategory) {
          // Questo controllo è superfluo, ma serve per soddisfare TypeScript
          this.categoriesWithProducts[this.selectedCategory] = response.content;
        }
        this.loading = false;
      },
      (error) => {
        console.error('Error fetching products by selected category:', error);
        this.loading = false;
      }
    );
  }

  get objectKeys(): Category[] {
    return Object.keys(this.categoriesWithProducts) as Category[];
  }
}
