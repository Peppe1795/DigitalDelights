import { Component, OnInit } from '@angular/core';
import { CartService } from 'src/app/services/cart.service';
import { OrderService } from 'src/app/services/order.service';
import { CartItem } from 'src/app/models/cart-item.interface';
import { ShippingInfo } from 'src/app/models/shipping-info.interface';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderComponent implements OnInit {
  cartItems: CartItem[] = [];
  totalPrice: number = 0;
  discount: number = 60;
  shippingCost: number = 14;

  shippingInfo: ShippingInfo = {
    recipientName: '',
    shippingAddress: '',
    city: '',
    state: '',
    postalCode: '',
    country: '',
    phoneNumber: '',
  };

  constructor(
    private cartService: CartService,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    this.cartService.getCartItems().subscribe(
      (items: CartItem[]) => {
        this.cartItems = items;
        this.calculateTotal();
      },
      (error) => {
        console.error('Error fetching cart items', error);
      }
    );
  }

  calculateTotal(): void {
    this.totalPrice = this.cartItems.reduce(
      (acc, item) => acc + item.product.price * item.quantity,
      0
    );
  }

  onContinueClick(): void {
    const tax = 0.1; // Supponiamo che la tassa sia del 10%. Modifica come necessario.
    this.orderService
      .createOrderWithShippingInfo(
        this.cartItems,
        this.totalPrice,
        tax,
        this.discount,
        this.shippingInfo
      )
      .subscribe(
        (response) => {
          console.log('Order created successfully', response);
        },
        (error) => {
          console.error('Error creating order', error);
        }
      );
  }

  onCancelClick(): void {
    // Resetta i valori del form
    this.shippingInfo = {
      recipientName: '',
      shippingAddress: '',
      city: '',
      state: '',
      postalCode: '',
      country: '',
      phoneNumber: '',
    };
  }
}
