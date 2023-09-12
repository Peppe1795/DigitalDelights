import { Component, OnInit } from '@angular/core';
import { CartItem } from 'src/app/models/cart-item.interface';
import { CartService } from 'src/app/services/cart.service';
import { Product } from 'src/app/models/products';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss'],
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];
  selectedProduct: Product | null = null; // Puoi lasciare "null" come valore iniziale
  quantity: number = 1; // Quantità predefinita

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.cartService.getCartItems().subscribe(
      (data) => {
        this.cartItems = data;
      },
      (error) => {
        console.error(
          'Errore nel recupero degli articoli del carrello:',
          error
        );
      }
    );
  }

  onRemove(productId: string): void {
    this.cartService.removeProduct(productId).subscribe(
      () => {
        // Rimuovi l'articolo dal carrello
        this.cartItems = this.cartItems.filter(
          (item) => item.product.productId !== productId
        );
      },
      (error) => {
        console.error(
          'Errore nella rimozione del prodotto dal carrello:',
          error
        );
      }
    );
  }
}
