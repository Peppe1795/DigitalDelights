import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Product } from '../models/products';
import { Category } from '../enum/category';
import { ApiResponse } from '../models/products';
import { AuthService } from '../auth/auth.service';
import { throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ProductRequestPayload } from '../models/productrequestpayload.interface';

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

  getAllProducts(page: number = 1): Observable<any> {
    const params = new HttpParams().set('page', page.toString());

    return this.http.get<any>(this.baseUrl, { params }).pipe(
      map((response) => {
        if (response && Array.isArray(response.content)) {
          return {
            products: response.content,
            totalPages: response.totalPages,
            currentPage: response.pageNumber,
          };
        } else {
          throw new Error('Invalid response structure');
        }
      }),
      catchError((error) => {
        console.error(
          'Errore durante la richiesta di tutti i prodotti:',
          error
        );
        return throwError(error);
      })
    );
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

  createProduct(product: ProductRequestPayload): Observable<Product> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http
      .post<Product>(`${this.baseUrl}`, JSON.stringify(product), { headers })
      .pipe(
        map((response) => {
          console.log('Product created:', response);
          return response;
        }),
        catchError((error) => {
          console.error('Error during product creation:', error);
          return throwError(error);
        })
      );
  }
}
