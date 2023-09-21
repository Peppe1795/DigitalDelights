import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Product } from '../models/products';
import { Category } from '../enum/category';
import { ApiResponse } from '../models/products';
import { AuthService } from '../auth/auth.service';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ProductsService {
  baseUrl = `${environment.baseURL}product`;
  baseURL = `${environment.baseURL}user`;
  constructor(private http: HttpClient, private authSrv: AuthService) {}

  getProducts(page: number, order: string): Observable<Product[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('order', order);

    return this.http
      .get<any>(this.baseUrl, { params })
      .pipe(map((response) => response.content));
  }

  getProductById(productId: string): Observable<Product> {
    return this.http.get<Product>(`${this.baseUrl}/${productId}`);
  }

  getProductsByCategory(
    category: Category,
    page?: number,
    size?: number
  ): Observable<ApiResponse> {
    let params = new HttpParams();
    if (page != null) {
      params = params.append('page', page.toString());
    }
    if (size != null) {
      params = params.append('size', size.toString());
    }
    return this.http.get<ApiResponse>(
      `${this.baseUrl}/filter?category=${category}`,
      { params }
    );
  }

  getFavorites(): Observable<any> {
    const userId = this.authSrv.getCurrentUserId();
    if (!userId) {
      console.error('User ID non trovato.');
      return throwError("Errore nel recupero dell'ID utente.");
    }

    const url = `${this.baseURL}/${userId}/wishList`;

    return this.http.get<string[]>(url);
  }
  addToFavorites(productId: string): Observable<any> {
    const url = `${this.baseURL}/addWishList/${productId}`;
    return this.http.post(url, {}, { responseType: 'text' });
  }

  removeFromFavorites(productId: string): Observable<any> {
    const url = `${this.baseURL}/removeWishList/${productId}`;
    return this.http.delete(url, { responseType: 'text' });
  }

  searchProducts(query: string, page?: number, size?: number): Observable<any> {
    let params = new HttpParams();
    if (page != null) {
      params = params.append('page', page.toString());
    }
    if (size != null) {
      params = params.append('size', size.toString());
    }
    return this.http
      .get<Product[]>(
        `${this.baseUrl}/partOfName?partOfName=${encodeURIComponent(query)}`,
        { params }
      )
      .pipe(
        map((response) => {
          console.log('Search response:', response);
          return response;
        }),
        catchError((error) => {
          console.error('Error during search:', error);
          return throwError(error);
        })
      );
  }
}
