import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ReviewsService {
  baseUrl = `${environment.baseURL}reviews`;

  constructor(private http: HttpClient) {}

  createReview(review: any): Observable<any> {
    return this.http.post(this.baseUrl, review);
  }
}
