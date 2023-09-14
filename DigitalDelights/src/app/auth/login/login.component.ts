import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authSrv: AuthService, private router: Router) {}

  onLogin(): void {
    this.authSrv.login(this.email, this.password).subscribe(
      (response) => {
        if (response.accessToken) {
          const role = this.authSrv.getUserRole();
          if (role === 'ADMIN') {
            this.router.navigate(['/dashboard']);
          } else {
            this.router.navigate(['/']);
          }
        }
      },
      (error) => {
        console.error('Errore durante il login:', error);
        this.errorMessage =
          error.error?.message || 'Email o password non valide. Riprova.';
      }
    );
  }
}
