import { Component, OnInit } from '@angular/core';
import { CartItem } from 'src/app/models/cart-item.interface';
import { CartService } from 'src/app/services/cart.service';
import { Product } from 'src/app/models/products';
import { OrderService } from 'src/app/services/order.service';

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

  constructor(
    private cartService: CartService,
    private orderSrv: OrderService
  ) {}

  ngOnInit(): void {
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
}
