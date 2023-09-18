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
  discount: number = 0;
  shippingCost: number = 14;
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
        this.orderService.setTotalPrice(this.totalPrice);
      },
      (error) => {
        console.error('Error fetching cart items', error);
      }
    );
    this.orderService.getTotalPrice().subscribe((price) => {
      if (price !== 0) {
        // Se il prezzo è diverso da 0, allora è stato caricato
        this.totalPrice = price;
        this.loaded = true; // Imposta il flag di caricamento a true
      }
    });
  }

  handlePayment() {
    if (this.loaded) {
      // Esegui il pagamento solo se i dati sono stati caricati
      this.stripeService.redirectToCheckout(this.totalPrice);
    } else {
      console.error('Total price data not loaded yet.');
    }
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
          this.isOrderCreated = true;
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
