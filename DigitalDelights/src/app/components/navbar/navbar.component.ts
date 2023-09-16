import { Component, OnInit } from '@angular/core';
import { Category } from 'src/app/enum/category';
import { ProductsService } from 'src/app/services/products.service';
import { AuthService } from 'src/app/auth/auth.service';
import { Product } from 'src/app/models/products';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  categoriesWithProducts: Category[] = [];
  searchResults: Product[] = [];
  searchQuery: string = '';

  constructor(
    private productSrv: ProductsService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCategoriesWithProducts();
  }

  logout(): void {
    this.authService.logout();
  }

  get isUserLoggedIn(): boolean {
    return this.authService.isAuthenticated();
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
          }
        },
        (error) => {
          console.error(
            `Error fetching products for category ${category}:`,
            error
          );
          completedRequests++;
          if (completedRequests === allCategories.length) {
          }
        }
      );
    });
  }

  get objectKeys(): Category[] {
    return this.categoriesWithProducts;
  }

  onSearchButtonClick(): void {
    if (this.searchQuery.trim() !== '') {
      this.router.navigate(['/findproducts'], {
        queryParams: { q: this.searchQuery },
      });
    }
  }
}
