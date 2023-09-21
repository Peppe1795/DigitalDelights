import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { switchMap, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { CartItem } from '../models/cart-item.interface';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private baseURL = `${environment.baseURL}cart`;

  constructor(private http: HttpClient) {}

  getCartItems(): Observable<CartItem[]> {
    return this.getCurrentUserCartId().pipe(
      switchMap((cartId) => {
        return this.http.get<CartItem[]>(`${this.baseURL}/${cartId}/products`);
      }),
      catchError((error) => {
        return throwError('Error fetching cart items');
      })
    );
  }

  getCurrentUserCartId(): Observable<string> {
    return this.http
      .get(`${this.baseURL}/current-user-cart-id`, { responseType: 'text' })
      .pipe(
        catchError((error) => {
          return throwError('Error fetching cart ID');
        })
      );
  }

  addProductToCart(productId: string, quantity: number): Observable<any> {
    return this.getCurrentUserCartId().pipe(
      switchMap((cartId) => {
        return this.http.post(
          `${this.baseURL}/${cartId}/product/${productId}?quantity=${quantity}`,
          null,
          { responseType: 'text' as 'json' }
        );
      }),
      catchError((error) => throwError(error))
    );
  }

  removeProduct(productId: string): Observable<any> {
    return this.getCurrentUserCartId().pipe(
      switchMap((cartId) => {
        return this.http.delete(
          `${this.baseURL}/${cartId}/product/${productId}`,
          { responseType: 'text' }
        );
      }),
      catchError((error) => throwError(error))
    );
  }

  updateProductQuantity(productId: string, quantity: number): Observable<any> {
    return this.getCurrentUserCartId().pipe(
      switchMap((cartId) => {
        return this.http.put(
          `${this.baseURL}/${cartId}/product/${productId}?quantity=${quantity}`,
          null,
          { responseType: 'text' as 'json' }
        );
      }),
      catchError((error) => throwError(error))
    );
  }

  clearCart(): Observable<any> {
    return this.getCurrentUserCartId().pipe(
      switchMap((cartId) => {
        return this.http.put(`${this.baseURL}/${cartId}/clear`, {});
      }),
      catchError((error) => throwError(error))
    );
  }
}
