import { Component, OnInit, ViewChild } from '@angular/core';
import { CartService } from 'src/app/services/cart.service';
import { OrderService } from 'src/app/services/order.service';
import { CartItem } from 'src/app/models/cart-item.interface';
import { ShippingInfo } from 'src/app/models/shipping-info.interface';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderComponent implements OnInit {
  cartItems: CartItem[] = [];
  totalPrice: number = 0;
  discount: number = 0;
  shippingCost: number = 14;
  promoCode: string = '';
  promoCodes: { [key: string]: number } = {
    PROMO10: 0.1,
    PROMO20: 0.2,
    PROMO30: 0.3,
  };
  shippingInfo: ShippingInfo = {
    recipientName: '',
    shippingAddress: '',
    city: '',
    state: '',
    postalCode: '',
    country: '',
    phoneNumber: '',
  };
  notificationMessage: string = '';

  @ViewChild('checkoutForm') checkoutForm!: NgForm;

  constructor(
    private cartService: CartService,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    this.cartService.getCartItems().subscribe(
      (items: CartItem[]) => {
        this.cartItems = items;
        this.calculateTotal();
        this.orderService.setTotalPrice(this.totalPrice);
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

  applyDiscount(): void {
    if (this.promoCodes[this.promoCode.toUpperCase()]) {
      this.discount =
        this.totalPrice * this.promoCodes[this.promoCode.toUpperCase()];
      this.calculateTotal();
    } else {
      alert('Codice promozionale non valido!');
      this.discount = 0;
      this.calculateTotal();
    }
  }

  onContinueClick(): void {
    if (!this.checkoutForm.valid) {
      alert('Per favore, completa tutti i campi richiesti.');
      return;
    }
    const tax = 0.1;
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
          this.notificationMessage = 'Ordine creato con successo!';

          // Reset the form and shipping info after order creation
          this.checkoutForm.reset();
          this.shippingInfo = {
            recipientName: '',
            shippingAddress: '',
            city: '',
            state: '',
            postalCode: '',
            country: '',
            phoneNumber: '',
          };
          this.promoCode = '';
          this.discount = 0;
          this.calculateTotal();

          setTimeout(() => {
            this.notificationMessage = '';
          }, 3000); // Removes the message after 3 seconds
        },
        (error) => {
          console.error('Error creating order', error);
          this.notificationMessage =
            "Errore nella creazione dell'ordine. Riprova.";
          setTimeout(() => {
            this.notificationMessage = '';
          }, 3000);
        }
      );
  }

  onCancelClick(): void {
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
