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
  showToast: boolean = false;

  constructor(private authSrv: AuthService, private router: Router) {}

  onLogin(): void {
    this.errorMessage = '';
    if (!this.email && !this.password) {
      this.errorMessage = 'Compila tutti i campi.';
      return;
    }

    if (!this.email) {
      this.errorMessage = 'Inserisci un indirizzo email.';
      return;
    }

    if (!this.password) {
      this.errorMessage = 'Inserisci una password.';
      return;
    }

    this.authSrv.login(this.email, this.password).subscribe(
      (response) => {
        if (response.accessToken) {
          this.router.navigate(['/']);
        }
      },
      (error) => {
        this.errorMessage = 'Errore durante il login.';
      }
    );
  }
}
