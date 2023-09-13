import { Component } from '@angular/core';
import { StripeService } from 'src/app/services/stripe.service';

@Component({
  selector: 'app-checkout',
  template: `
    <button (click)="handlePayment()" class="btn btn-primary">
      Go to Stripe Checkout
    </button>
  `,
})
export class CheckoutComponent {
  constructor(private stripeService: StripeService) {}

  handlePayment() {
    this.stripeService.redirectToCheckout();
  }
}
