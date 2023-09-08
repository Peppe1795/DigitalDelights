import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-singin',
  templateUrl: './singin.component.html',
  styleUrls: ['./singin.component.scss'],
})
export class SinginComponent implements OnInit {
  isLoading = false;

  constructor(private authSrv: AuthService, private router: Router) {}

  ngOnInit(): void {}

  registra(form: NgForm) {
    this.isLoading = true;
    console.log(form.value);
    try {
      this.authSrv.signup(form.value).subscribe();
      this.router.navigate(['/login']);
      this.isLoading = false;
    } catch (error: any) {
      console.error(error);
      if (error.status == 400) {
        alert('Email già registrata!');
        this.router.navigate(['/register']);
      }
      this.isLoading = false;
    }
  }
}
