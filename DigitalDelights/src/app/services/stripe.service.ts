import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { loadStripe, Stripe } from '@stripe/stripe-js';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class StripeService {
  private stripePromise: Promise<Stripe | null>;
  private baseURL = `${environment.baseURL}stripe`;

  constructor(private http: HttpClient) {
    this.stripePromise = loadStripe(
      'pk_test_51NprL8Kc0MLnpKNVDBLh9GtMebUbUw0KgxdfOtGp0YAi7VRVBtPUnG7tDptZtP3TfOoi3TNsYwE07P0tctbEyuDX00PPMZuWO6'
    );
  }

  async getStripe(): Promise<Stripe> {
    const stripe = await this.stripePromise;
    if (!stripe) {
      throw new Error('Failed to load Stripe.');
    }
    return stripe;
  }

  async redirectToCheckout() {
    const session = await this.http
      .get<any>(`${this.baseURL}/create-checkout-session`)
      .toPromise();
    const stripe = await this.getStripe();
    const { error } = await stripe.redirectToCheckout({
      sessionId: session.id,
    });

    if (error) {
      console.error(error);
    }
  }
}
