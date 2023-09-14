import { Component, OnInit } from '@angular/core';
import { StripeService } from 'src/app/services/stripe.service';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-checkout',
  template: `
    <button (click)="handlePayment()" class="btn btn-primary">
      Go to Stripe Checkout
    </button>
  `,
})
export class CheckoutComponent implements OnInit {
  totalPrice: number = 0;
  loaded: boolean = false; // Nuova variabile per tracciare lo stato di caricamento

  constructor(
    private stripeService: StripeService,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
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
}
