import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { environment } from 'src/environments/environment';
import { HttpParams } from '@angular/common/http';
import { WishList } from '../models/wish-list.interface';
@Injectable({
  providedIn: 'root',
})
export class WishListService {
  private baseUrl = `${environment.baseURL}user`;

  constructor(private http: HttpClient) {}

  getUserWishlist(
    userId: string,
    page: number,
    size: number
  ): Observable<WishList> {
    const url = `${this.baseUrl}/${userId}/wishList`;
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<WishList>(url, { params });
  }
}
