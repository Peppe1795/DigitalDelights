import { Component, OnInit } from '@angular/core';
import { WishListService } from 'src/app/services/wish-list.service';
import { Product } from 'src/app/models/products';
import { AuthService } from 'src/app/auth/auth.service';
import { CartService } from 'src/app/services/cart.service';
import { Observable } from 'rxjs';
import { ChangeDetectorRef } from '@angular/core';
import { ProductsService } from 'src/app/services/products.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-wish-list',
  templateUrl: './wish-list.component.html',
  styleUrls: ['./wish-list.component.scss'],
})
export class WishListComponent implements OnInit {
  favorites: Product[] = [];
  currentPage = 0;
  pageSize = 10;
  favoriteProductIds: string[] = [];
  constructor(
    private wishlistService: WishListService,
    private authSrv: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private productSrv: ProductsService,
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    this.loadFavorites();
    const userId = this.authSrv.getCurrentUserId();
    if (userId) {
      this.loadWishlist(userId);
    } else {
      console.error(
        "L'utente non è autenticato o l'ID utente non è disponibile."
      );
    }
  }

  loadWishlist(userId: string): void {
    this.wishlistService
      .getUserWishlist(userId, this.currentPage, this.pageSize)
      .subscribe(
        (response: any) => {
          this.favorites = response.content;
        },
        (error) => {
          console.error('Errore nel recupero dei preferiti:', error);
        }
      );
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

  loadMoreProducts(): void {
    // Incrementa la pagina corrente prima di caricare più prodotti
    this.currentPage++;
    const userId = this.authSrv.getCurrentUserId();
    if (userId) {
      this.loadWishlist(userId);
    }
  }
  toggleFavorite(product: Product): void {
    if (!product) {
      console.error('Nessun prodotto fornito.');
      return;
    }

    if (!this.authSrv.isAuthenticated()) {
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

          this.favorites = this.favorites.filter(
            (favProduct) => favProduct.productId !== product.productId
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
}
