import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  isLoading = false;
  email: string = '';
  password: string = '';
  errorMessage: string = '';
  constructor(private authSrv: AuthService, private router: Router) {}

  ngOnInit(): void {}

  login(): void {
    this.authSrv.login(this.email, this.password).subscribe(
      (response) => {
        if (response.accessToken) {
          this.router.navigate(['/home']); // reindirizza l'utente alla dashboard o alla home dopo il login
        }
      },
      (error) => {
        this.errorMessage = 'Email o password non valide. Riprova.'; // mostrare questo messaggio di errore nel template
      }
    );
  }
  redirectToRegister() {
    this.router.navigate(['/singin']);
  }
}
