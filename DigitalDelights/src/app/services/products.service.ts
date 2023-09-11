import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Product } from '../models/products';
import { Category } from '../enum/category';
import { ApiResponse } from '../models/products';

@Injectable({
  providedIn: 'root',
})
export class ProductsService {
  baseUrl = `${environment.baseURL}product`;

  constructor(private http: HttpClient) {}

  getProducts(page: number, order: string): Observable<Product[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('order', order);

    return this.http
      .get<any>(this.baseUrl, { params })
      .pipe(map((response) => response.content));
  }

  getProductsByCategory(category: Category): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(
      `${this.baseUrl}/filter?category=${category}`
    );
  }
}
