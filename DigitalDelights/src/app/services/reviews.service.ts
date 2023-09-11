import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ReviewsService {
  baseUrl = `${environment.baseURL}reviews/product/{productId}`;

  constructor(private http: HttpClient) {}

  createReview(productId: string, review: any): Observable<any> {
    // Aggiungi l'ID del prodotto al percorso dell'endpoint
    const endpoint = `${this.baseUrl}/${productId}`;

    return this.http.post(endpoint, review);
  }
}
