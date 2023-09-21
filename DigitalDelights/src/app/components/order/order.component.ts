import { Component, OnInit, ViewChild } from '@angular/core';
import { CartService } from 'src/app/services/cart.service';
import { OrderService } from 'src/app/services/order.service';
import { CartItem } from 'src/app/models/cart-item.interface';
import { ShippingInfo } from 'src/app/models/shipping-info.interface';
import { NgForm } from '@angular/forms';
import { StripeService } from 'src/app/services/stripe.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderComponent implements OnInit {
  cartItems: CartItem[] = [];
  totalPrice: number = 0;
  originalTotalPrice: number = 0;

  discount: number = 0;
  promoCode: string = '';
  isOrderCreated: boolean = false;
  promoCodes: { [key: string]: number } = {
    PROMO10: 0.1,
    PROMO20: 0.2,
    PROMO30: 0.3,
  };
  loaded: boolean = false;
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
    private orderService: OrderService,
    private stripeService: StripeService
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

  handlePayment() {
    if (this.loaded) {
      this.stripeService.redirectToCheckout(this.totalPrice);
    } else {
      console.error('Total price data not loaded yet.');
    }
  }

  calculateTotalWithoutDiscount(): number {
    return this.cartItems.reduce(
      (acc, item) => acc + item.product.price * item.quantity,
      0
    );
  }

  calculateTotal(): void {
    const baseTotal = this.calculateTotalWithoutDiscount();
    if (!this.originalTotalPrice) {
      this.originalTotalPrice = baseTotal;
    }
    if (this.promoCodes[this.promoCode.toUpperCase()]) {
      this.discount = baseTotal * this.promoCodes[this.promoCode.toUpperCase()];
    } else {
      this.discount = 0;
    }
    this.totalPrice = baseTotal - this.discount;
  }

  applyDiscount(): void {
    if (this.promoCodes[this.promoCode.toUpperCase()]) {
      this.calculateTotal();
      console.log('Total Price after discount:', this.totalPrice);
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
          this.isOrderCreated = true;
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
          this.loaded = true;
          setTimeout(() => {
            this.notificationMessage = '';
          }, 3000);
          this.cartService.clearCart().subscribe(
            () => {
              console.log('Il carrello è stato svuotato con successo.');
            },
            (error) => {
              console.error('Errore nello svuotamento del carrello:', error);
            }
          );
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
