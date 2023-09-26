import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';
@Injectable({
  providedIn: 'root',
})
export class GuardGuard implements CanActivate {
  constructor(private autSrv: AuthService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    if (this.autSrv.isAuthenticated()) {
      return true;
    }
    alert(
      'Per visualizzare questa risorsa devi essere loggato!\nAccedi o registrati'
    );
    return this.router.createUrlTree(['/login']);
  }
}
