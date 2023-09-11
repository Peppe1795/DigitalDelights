import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, throwError } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Data } from './data.interface';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

interface DecodedToken {
  sub: string;
  iat: number;
  exp: number;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  jwtHelper = new JwtHelperService();
  baseURL = environment.baseURL;
  private authSubj = new BehaviorSubject<null | Data>(null);

  user$ = this.authSubj.asObservable();
  timeoutLogout: any;

  constructor(private http: HttpClient, private router: Router) {}

  login(email: string, password: string): Observable<any> {
    const credentials = { email, password };
    return this.http.post<any>(`${this.baseURL}auth/login`, credentials).pipe(
      map((response) => {
        console.log('Server Response:', response);
        if (response.accessToken) {
          console.log('Token:', response.accessToken);
          localStorage.setItem('token', response.accessToken);

          // Se la tua risposta ha un campo user, aggiungi questo:
          if (response.user) {
            localStorage.setItem('user', JSON.stringify(response.user)); // Salva l'utente nel localStorage
            this.authSubj.next(response.user);
          }
        }
        return response;
      })
    );
  }

  restore() {
    const token = localStorage.getItem('token');
    if (!token) {
      return;
    }
    const decodedToken = this.jwtHelper.decodeToken(token);
    if (this.jwtHelper.isTokenExpired(token)) {
      return;
    }
    this.authSubj.next(decodedToken);
    this.autoLogout(decodedToken); // Qui potrebbe esserci bisogno di adattamenti
  }

  signup(data: {
    name: string;
    username: string;
    lastName: string;
    email: string;
    password: string;
    via: string;
    numeroCivico: number;
    localita: string;
    cap: string;
    comune: string;
  }) {
    return this.http.post(`${this.baseURL}auth/register`, data);
  }

  logout() {
    this.authSubj.next(null);
    localStorage.removeItem('user');
    this.router.navigate(['/login']);
    if (this.timeoutLogout) {
      clearTimeout(this.timeoutLogout);
    }
  }

  autoLogout(data: Data) {
    const expirationDate = this.jwtHelper.getTokenExpirationDate(
      data.accessToken
    ) as Date;
    const expirationMilliseconds =
      expirationDate.getTime() - new Date().getTime();
    this.timeoutLogout = setTimeout(() => {
      this.logout();
    }, expirationMilliseconds);
  }

  private errors(err: any) {
    switch (err.error) {
      case 'Email already exists':
        return throwError('Utente già presente');
      case 'Email format is invalid':
        return throwError('Formato mail non valido');
      default:
        return throwError('Errore nella chiamata');
    }
  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    return !!token; // Restituisce true se il token è presente, altrimenti false
  }
  getCurrentUserId(): string | null {
    const token = localStorage.getItem('token');
    if (!token) {
      console.error('Token non trovato nel localStorage.');
      return null;
    }

    try {
      const decodedToken: DecodedToken | null =
        this.jwtHelper.decodeToken(token);

      // Se decodedToken è null, restituisci null.
      if (!decodedToken) {
        console.error('Token decodificato è null.');
        return null;
      }

      return decodedToken.sub; // Questo dovrebbe restituire l'UUID dell'utente.
    } catch (error) {
      console.error('Errore nella decodifica del token:', error);
      return null;
    }
  }
}
