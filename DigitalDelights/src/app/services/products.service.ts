import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators'; // <-- NOTA questa modifica
import { Product } from '../models/products';

@Injectable({
  providedIn: 'root',
})
export class ProductsService {
  baseUrl = `${environment.baseURL}product`;

  constructor(private http: HttpClient) {}

  getProducts(page: Number, order: string): Observable<Product[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('order', order);

    return this.http
      .get<any>(this.baseUrl, { params })
      .pipe(map((response) => response.content));
  }
}
