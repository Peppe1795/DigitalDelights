import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { CartItem } from '../models/cart-item.interface';
import { environment } from 'src/environments/environment';
import { AuthService } from '../auth/auth.service';
import { ShippingInfo } from '../models/shipping-info.interface';
import { BehaviorSubject } from 'rxjs';
import { Orders } from '../models/orders.interface';
import { DetailedOrderResponse } from '../models/detailedorderresponse.interface';
import { OrderResponse } from '../models/orderresponse.interface';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private baseURL = `${environment.baseURL}orders`;
  private shippingURL = `${environment.baseURL}shipping`;
  private totalPriceSubject: BehaviorSubject<number> =
    new BehaviorSubject<number>(0);
  public totalPrice$: Observable<number> =
    this.totalPriceSubject.asObservable();

  constructor(private http: HttpClient, private authSrv: AuthService) {}

  createShippingInfo(shippingInfo: ShippingInfo): Observable<ShippingInfo> {
    return this.http.post<ShippingInfo>(`${this.shippingURL}`, shippingInfo);
  }

  createOrderWithShippingInfo(
    cartItems: CartItem[],
    totalPrice: number,
    tax: number,
    discount: number,
    shippingInfo: ShippingInfo
  ): Observable<any> {
    const orderPayload = {
      userId: this.authSrv.getCurrentUserId(),
      shippingInfoRequestPayload: shippingInfo,
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
  getAllOrders(): Observable<OrderResponse> {
    return this.http.get<OrderResponse>(`${this.baseURL}`);
  }

  getTotalPrice(): Observable<number> {
    return this.totalPrice$;
  }
  getMyOrders(): Observable<Orders[]> {
    return this.http.get<Orders[]>(`${this.baseURL}/my-orders`);
  }

  getOrderDetails(orderId: string): Observable<DetailedOrderResponse> {
    return this.http.get<DetailedOrderResponse>(
      `${this.baseURL}/${orderId}/details`
    );
  }

  deleteOrder(orderId: string): Observable<any> {
    return this.http.delete(`${this.baseURL}/${orderId}`, {
      responseType: 'text',
    });
  }

  shipOrder(orderId: string): Observable<any> {
    return this.http.put(`${this.baseURL}/${orderId}/ship`, {});
  }
}
