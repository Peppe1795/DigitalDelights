import { Component, OnInit } from '@angular/core';
import { CartItem } from 'src/app/models/cart-item.interface';
import { CartService } from 'src/app/services/cart.service';
import { Product } from 'src/app/models/products';
import { AuthService } from 'src/app/auth/auth.service';
import { ProductsService } from 'src/app/services/products.service';
import { Observable } from 'rxjs';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss'],
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];
  selectedProduct: Product | null = null;
  quantity: number = 1;

  totalPrice: number = 0;
  discount: number = 0;
  tax: number = 0.05;
  finalTotal: number = 0;
  favoriteProductIds: string[] = [];

  constructor(
    private cartService: CartService,
    public authService: AuthService,
    private productSrv: ProductsService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadFavorites();
    this.cartService.getCartItems().subscribe(
      (data) => {
        this.cartItems = data;
        this.calculateTotals();
      },
      (error) => {
        console.error(
          'Errore nel recupero degli articoli del carrello:',
          error
        );
      }
    );
  }

  calculateTotals(): void {
    this.totalPrice = this.cartItems.reduce(
      (sum, item) => sum + item.product.price * item.quantity,
      0
    );
    this.finalTotal =
      this.totalPrice - this.discount + this.totalPrice * this.tax;
  }

  onRemove(productId: string): void {
    this.cartService.removeProduct(productId).subscribe(
      () => {
        this.cartItems = this.cartItems.filter(
          (item) => item.product.productId !== productId
        );
        this.calculateTotals(); // Update totals whenever an item is removed.
      },
      (error) => {
        console.error(
          'Errore nella rimozione del prodotto dal carrello:',
          error
        );
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
}
