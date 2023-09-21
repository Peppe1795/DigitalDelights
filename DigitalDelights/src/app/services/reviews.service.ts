import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ReviewsService {
  baseUrl = `${environment.baseURL}reviews/product`;

  constructor(private http: HttpClient) {}

  createReview(
    userId: string,
    productId: string,
    review: any
  ): Observable<any> {
    const endpoint = `${this.baseUrl}/${productId}/user/${userId}`;
    return this.http.post(endpoint, review);
  }

  getReviewsByProductId(productId: string): Observable<any> {
    const endpoint = `${this.baseUrl}/${productId}`;
    return this.http.get(endpoint);
  }
}
