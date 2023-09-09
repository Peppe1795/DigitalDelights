import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-singin',
  templateUrl: './singin.component.html',
  styleUrls: ['./singin.component.scss'],
})
export class SinginComponent implements OnInit {
  isLoading = false;

  formData: any = {
    name: '',
    username: '',
    lastName: '',
    email: '',
    password: '',
    address: {
      via: '',
      numeroCivico: null,
      localita: '',
      cap: '',
      comune: '',
    },
  };

  constructor(private authSrv: AuthService, private router: Router) {}

  ngOnInit(): void {}

  registra() {
    this.isLoading = true;
    console.log(this.formData);
    this.authSrv.signup(this.formData).subscribe(
      () => {
        this.router.navigate(['/login']);
        this.isLoading = false;
      },
      (error) => {
        console.error(error);
        if (error.status == 400) {
          alert('Email già registrata!');
          this.router.navigate(['/register']);
        }
        this.isLoading = false;
      }
    );
  }
}
