import { Component, OnInit } from '@angular/core';
import { OrderService } from 'src/app/services/order.service';
import { Orders } from 'src/app/models/orders.interface';
import { UserProfile } from 'src/app/models/userprofile.interface';
import { AuthService } from 'src/app/auth/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  orders: Orders[] = [];
  user: UserProfile | null = null;
  userForm!: FormGroup;

  constructor(
    private orderService: OrderService,
    private authService: AuthService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loadMyOrders();
    this.loadUserData();
    this.initUserForm();
  }

  initUserForm() {
    this.userForm = this.fb.group({
      name: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.minLength(8)]],
      address: this.fb.group({
        via: ['', Validators.required],
        numeroCivico: ['', Validators.required],
        localita: ['', Validators.required],
        cap: ['', Validators.required],
        comune: ['', Validators.required],
      }),
    });
  }

  loadMyOrders(): void {
    this.orderService.getMyOrders().subscribe(
      (data) => {
        this.orders = data;
      },
      (error) => {
        console.error('Errore nel caricamento degli ordini:', error);
      }
    );
  }

  loadUserData(): void {
    const userId = this.authService.getCurrentUserId();
    if (userId) {
      this.authService.getUserDetails(userId).subscribe(
        (response: UserProfile) => {
          this.user = response;
          console.log('Dettagli utente ricevuti:', this.user);
        },
        (error) => {
          console.error("Errore nel recuperare i dettagli dell'utente:", error);
        }
      );
    }
  }

  onSubmit() {
    if (this.userForm.valid) {
      const updatedUserDetails = {
        ...this.user,
        ...this.userForm.value,
      };

      this.authService.updateUserDetails(updatedUserDetails).subscribe(
        (response) => {
          console.log('Dettagli utente aggiornati con successo:', response);
          alert('Dettagli utente aggiornati con successo!');
        },
        (error) => {
          console.error(
            "Errore durante l'aggiornamento dei dettagli dell'utente:",
            error
          );
          alert("Si è verificato un errore durante l'aggiornamento. Riprova.");
        }
      );
    } else {
      alert('Assicurati che tutti i campi siano compilati correttamente.');
    }
  }
}
