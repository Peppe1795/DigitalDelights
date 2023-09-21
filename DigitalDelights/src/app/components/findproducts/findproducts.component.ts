import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductsService } from 'src/app/services/products.service';
import { Product } from 'src/app/models/products';
import { CartService } from 'src/app/services/cart.service';
import { AuthService } from 'src/app/auth/auth.service';
import { Observable } from 'rxjs';
import { ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-findproducts',
  templateUrl: './findproducts.component.html',
  styleUrls: ['./findproducts.component.scss'],
})
export class FindproductsComponent implements OnInit {
  products: Product[] = [];
  searchQuery: string = '';
  favoriteProductIds: string[] = [];

  // Variabili per la paginazione
  currentPage: number = 1;
  pageSize: number = 10;
  totalPages: number = 0;

  constructor(
    private route: ActivatedRoute,
    private productSrv: ProductsService,
    private cartService: CartService,
    public authService: AuthService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadFavorites();
    this.route.queryParams.subscribe((params) => {
      this.searchQuery = params['q'];
      this.loadProducts();
    });
  }

  loadProducts(): void {
    this.productSrv
      .searchProducts(this.searchQuery, this.currentPage, this.pageSize)
      .subscribe((response: any) => {
        if (Array.isArray(response)) {
          this.products = response;
        } else if (response && response.content) {
          this.products = response.content;
          this.totalPages = response.totalPages;
        }
      });
  }

  loadFavorites(): void {
    this.productSrv.getFavorites().subscribe(
      (response: any) => {
        this.favoriteProductIds = response.content.map(
          (product: any) => product.productId
        );
        console.log('Favorites loaded:', this.favoriteProductIds);
      },
      (error) => {
        console.error('Errore nel caricamento dei prodotti preferiti:', error);
      }
    );
  }

  addToCart(product: Product): void {
    if (!product) {
      console.error('Nessun prodotto fornito.');
      return;
    }

    this.cartService.addProductToCart(product.productId, 1).subscribe(
      () => {
        console.log('Prodotto aggiunto al carrello con successo!');
      },
      (error) => {
        console.error(
          'Errore:',
          error.message || "Errore nell'aggiunta del prodotto al carrello."
        );
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
        console.log('Prodotti preferiti aggiornati:', this.favoriteProductIds);
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

  goPrevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.loadProducts();
    }
  }

  goNextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.loadProducts();
    }
  }
}
