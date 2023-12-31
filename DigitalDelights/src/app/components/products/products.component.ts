import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Product } from 'src/app/models/products';
import { ProductsService } from 'src/app/services/products.service';
import { Category } from 'src/app/enum/category';
import { ReviewsService } from 'src/app/services/reviews.service';
import { AuthService } from 'src/app/auth/auth.service';
import { CartService } from 'src/app/services/cart.service';
import { Observable } from 'rxjs';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent implements OnInit {
  categoriesWithProducts: { [key: string]: Product[] } = {};
  objectKeys = Object.keys;
  loading = true;
  selectedCategory?: Category;
  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;

  favoriteProductIds: string[] = [];

  constructor(
    private productSrv: ProductsService,
    private route: ActivatedRoute,
    public authService: AuthService,
    public reviewsService: ReviewsService,
    private cartService: CartService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadFavorites();

    this.route.paramMap.subscribe((paramMap) => {
      const category = paramMap.get('category');
      if (category) {
        this.selectedCategory = category as Category;

        this.loadProductsBySelectedCategory();
      } else {
        this.loadAllProductsByCategories();
      }
    });
  }
  getObjectKeys(obj: any): string[] {
    return Object.keys(obj);
  }

  goPrevPage(): void {
    this.currentPage--;
    this.loadProductsBySelectedCategory();
  }

  goNextPage(): void {
    this.currentPage++;
    this.loadProductsBySelectedCategory();
  }
  loadAllProductsByCategories(): void {
    const categories = Object.values(Category);

    let completedRequests = 0;

    categories.forEach((category) => {
      this.productSrv
        .getProductsByCategory(category, this.currentPage, this.pageSize)
        .subscribe(
          (response) => {
            this.categoriesWithProducts[category] = response.content;

            this.totalPages = response.totalPages;

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
  loadFavorites(): void {
    this.productSrv.getFavorites().subscribe(
      (response: any) => {
        this.favoriteProductIds = response.content.map(
          (product: any) => product.productId
        );
      },
      (error) => {
        console.error('Errore nel caricamento dei prodotti preferiti:', error);
      }
    );
  }

  addToCart(product: Product): void {
    if (!product) {
      console.error('Nessun prodotto fornito.');
      alert('Errore: Nessun prodotto fornito.');
      return;
    }

    this.cartService.addProductToCart(product.productId, 1).subscribe(
      () => {
        ('Prodotto aggiunto al carrello con successo!');
        alert('Prodotto aggiunto al carrello con successo!');
      },
      (error) => {
        console.error(
          'Errore:',
          error.message || "Errore nell'aggiunta del prodotto al carrello."
        );
        alert('Devi essere loggato!\nAccedi o registrati');
      }
    );
  }

  loadProductsBySelectedCategory(): void {
    if (!this.selectedCategory) {
      console.error('Categoria selezionata non definita!');
      return;
    }

    this.categoriesWithProducts = {};
    this.productSrv
      .getProductsByCategory(
        this.selectedCategory,
        this.currentPage,
        this.pageSize
      )
      .subscribe(
        (response) => {
          if (this.selectedCategory) {
            this.categoriesWithProducts[this.selectedCategory] =
              response.content;
            this.totalPages = response.totalPages;
          }
          this.loading = false;
        },
        (error) => {
          console.error(
            'Errore nel caricamento dei prodotti per la categoria selezionata:',
            error
          );
          this.loading = false;
        }
      );
  }

  toggleFavorite(product: Product): void {
    if (!product) {
      console.error('Nessun prodotto fornito.');
      return;
    }

    if (!this.authService.isAuthenticated()) {
      console.error("L'utente non è autenticato. Devi effettuare il login.");
      return;
    }

    let actionObservable: Observable<any>;

    if (this.favoriteProductIds.includes(product.productId)) {
      actionObservable = this.productSrv.removeFromFavorites(product.productId);
    } else {
      actionObservable = this.productSrv.addToFavorites(product.productId);
    }

    actionObservable.subscribe(
      () => {
        if (this.favoriteProductIds.includes(product.productId)) {
          this.favoriteProductIds = this.favoriteProductIds.filter(
            (id) => id !== product.productId
          );
        } else {
          this.favoriteProductIds.push(product.productId);
        }

        this.cdr.detectChanges();
      },
      (error) => {
        console.error(
          'Errore:',
          error.message || "Errore nell'aggiornamento dei prodotti preferiti."
        );
      }
    );
  }

  showDetails(product: Product): void {
    this.router.navigate(['/details', product.productId]);
  }
}
