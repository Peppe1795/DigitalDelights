import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthService } from '../auth/auth.service';
import { catchError, throwError } from 'rxjs';
import { switchMap } from 'rxjs';
import { CartItem } from '../models/cart-item.interface';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private baseURL = `${environment.baseURL}cart`;

  constructor(private http: HttpClient, private authService: AuthService) {}

  getCartItems(): Observable<CartItem[]> {
    return this.getCurrentUserCartId().pipe(
      switchMap((cartId) => {
        return this.http.get<CartItem[]>(`${this.baseURL}/${cartId}/products`);
      }),
      catchError((error) => {
        console.error('Error fetching cart items', error);
        return throwError('Error fetching cart items');
      })
    );
  }

  getCurrentUserCartId(): Observable<string> {
    return this.http
      .get(`${this.baseURL}/current-user-cart-id`, {
        responseType: 'text',
      })
      .pipe(
        catchError((error) => {
          console.error('Error fetching cart ID', error);
          return throwError('Error fetching cart ID');
        })
      );
  }

  addProductToCart(productId: string, quantity: number): Observable<any> {
    return this.getCurrentUserCartId().pipe(
      switchMap((cartId) => {
        if (!cartId) {
          console.error("ID del carrello dell'utente non disponibile.");
          return throwError("ID del carrello dell'utente non disponibile.");
        }
        console.log(cartId);

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
        if (!cartId) {
          console.error("ID del carrello dell'utente non disponibile.");
          return throwError("ID del carrello dell'utente non disponibile.");
        }

        return this.http.delete(
          `${this.baseURL}/${cartId}/product/${productId}`,
          {
            responseType: 'text',
          }
        );
      }),
      catchError((error) => throwError(error))
    );
  }

  updateProductQuantity(productId: string, quantity: number): Observable<any> {
    return this.getCurrentUserCartId().pipe(
      switchMap((cartId) => {
        if (!cartId) {
          console.error("ID del carrello dell'utente non disponibile.");
          return throwError("ID del carrello dell'utente non disponibile.");
        }

        return this.http.put(
          `${this.baseURL}/${cartId}/product/${productId}?quantity=${quantity}`,
          null,
          { responseType: 'text' as 'json' }
        );
      }),
      catchError((error) => throwError(error))
    );
  }
}
