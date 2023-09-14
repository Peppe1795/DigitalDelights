import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import { Data } from './data.interface';
import { map } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';

interface DecodedToken {
  sub: string;
  iat: number;
  exp: number;
  role: string;
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  jwtHelper = new JwtHelperService();
  baseURL = environment.baseURL;

  private userRoleSubject = new BehaviorSubject<string | null>(null);
  userRole$ = this.userRoleSubject.asObservable();

  private userSubject = new BehaviorSubject<any>(null); // Aggiunto userSubject
  user$ = this.userSubject.asObservable();

  timeoutLogout: any;

  constructor(private http: HttpClient, private router: Router) {}

  login(email: string, password: string): Observable<any> {
    const credentials = { email, password };
    return this.http.post<Data>(`${this.baseURL}auth/login`, credentials).pipe(
      map((response) => {
        console.log('Server Response:', response);
        if (response.accessToken) {
          localStorage.setItem('token', response.accessToken);

          // Correzione: impostazione diretta del ruolo dalla risposta
          this.userRoleSubject.next(response.role);

          const decodedUser = this.jwtHelper.decodeToken(response.accessToken);
          this.userSubject.next(decodedUser);
        }
        return response;
      }),
      catchError((error: any) => {
        console.error('Dettaglio errore dal server:', error);
        throw error;
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    this.userSubject.next(null); // Reset valore userSubject
    this.userRoleSubject.next(null);
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    return !this.jwtHelper.isTokenExpired(token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  restore(): void {
    const token = this.getToken();
    if (token && !this.jwtHelper.isTokenExpired(token)) {
      this.userSubject.next({ token }); // Aggiunto il restore dell'utente basato sul token
    }
  }

  getUserRole(): string | null {
    const token = this.getToken();
    if (!token) {
      console.error('Token non trovato nel localStorage.');
      return null;
    }

    try {
      const decodedToken: DecodedToken | null =
        this.jwtHelper.decodeToken(token);
      if (!decodedToken || !decodedToken.role) {
        console.error('Il ruolo non è presente nel token decodificato.');
        return null;
      }
      return decodedToken.role;
    } catch (error) {
      console.error('Errore nella decodifica del token:', error);
      return null;
    }
  }

  getUser(): Observable<any> {
    return this.userSubject.asObservable(); // Fornisce l'Observable per l'utente
  }

  signup(data: Data) {
    return this.http.post(`${this.baseURL}auth/register`, data);
  }

  autoLogout(accessToken: string) {
    const expirationDate = this.jwtHelper.getTokenExpirationDate(
      accessToken
    ) as Date;
    const expirationMilliseconds =
      expirationDate.getTime() - new Date().getTime();
    this.timeoutLogout = setTimeout(() => {
      this.logout();
    }, expirationMilliseconds);
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
      if (!decodedToken) {
        console.error('Token decodificato è null.');
        return null;
      }

      return decodedToken.sub;
    } catch (error) {
      console.error('Errore nella decodifica del token:', error);
      return null;
    }
  }

  isAdmin(): boolean {
    return this.userRoleSubject.value === 'ADMIN';
  }

  isUser(): boolean {
    return this.userRoleSubject.value === 'USER';
  }
}
