import { Component, OnInit } from '@angular/core';
import { Category } from 'src/app/enum/category';
import { ProductsService } from 'src/app/services/products.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  categoriesWithProducts: Category[] = [];

  constructor(private productSrv: ProductsService) {}

  ngOnInit(): void {
    this.loadCategoriesWithProducts();
  }

  loadCategoriesWithProducts(): void {
    const allCategories = Object.values(Category);
    let completedRequests = 0;

    allCategories.forEach((category) => {
      this.productSrv.getProductsByCategory(category).subscribe(
        (response) => {
          if (response && response.content && response.content.length > 0) {
            this.categoriesWithProducts.push(category);
          }
          completedRequests++;
          if (completedRequests === allCategories.length) {
            // Tutte le richieste sono complete, puoi eseguire qualsiasi altra logica qui se necessario
          }
        },
        (error) => {
          console.error(
            `Error fetching products for category ${category}:`,
            error
          );
          completedRequests++;
          if (completedRequests === allCategories.length) {
            // Tutte le richieste sono complete, puoi eseguire qualsiasi altra logica qui se necessario
          }
        }
      );
    });
  }

  get objectKeys(): Category[] {
    return this.categoriesWithProducts;
  }
}
