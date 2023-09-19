import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { CartItem } from '../models/cart-item.interface';
import { environment } from 'src/environments/environment';
import { AuthService } from '../auth/auth.service';
import { ShippingInfo } from '../models/shipping-info.interface';
import { BehaviorSubject } from 'rxjs';
import { Orders } from '../models/orders.interface';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private baseURL = `${environment.baseURL}orders`;
  private shippingURL = `${environment.baseURL}shipping`; // URL per le chiamate API di spedizione
  private totalPriceSubject: BehaviorSubject<number> =
    new BehaviorSubject<number>(0);
  public totalPrice$: Observable<number> =
    this.totalPriceSubject.asObservable();

  constructor(private http: HttpClient, private authSrv: AuthService) {}

  // Metodo per creare le informazioni di spedizione
  createShippingInfo(shippingInfo: ShippingInfo): Observable<ShippingInfo> {
    return this.http.post<ShippingInfo>(`${this.shippingURL}`, shippingInfo);
  }

  // Metodo per creare un ordine con le informazioni di spedizione
  createOrderWithShippingInfo(
    cartItems: CartItem[],
    totalPrice: number,
    tax: number,
    discount: number,
    shippingInfo: ShippingInfo
  ): Observable<any> {
    const orderPayload = {
      userId: this.authSrv.getCurrentUserId(),
      shippingInfoRequestPayload: shippingInfo, // Invia l'intero oggetto ShippingInfo
      totalPrice: totalPrice + totalPrice * tax - discount,
      orderItems: cartItems.map((item) => ({
        productId: item.product.productId,
        quantity: item.quantity,
      })),
    };
    return this.http.post(`${this.baseURL}`, orderPayload);
  }
  setTotalPrice(price: number): void {
    this.totalPriceSubject.next(price);
  }

  getTotalPrice(): Observable<number> {
    return this.totalPrice$;
  }
  getMyOrders(): Observable<Orders[]> {
    return this.http.get<Orders[]>(`${this.baseURL}/my-orders`);
  }
}
