import { Component, OnInit } from '@angular/core';
import { OrderService } from 'src/app/services/order.service';
import { Orders } from 'src/app/models/orders.interface';
import { UserProfile } from 'src/app/models/userprofile.interface';
import { AuthService } from 'src/app/auth/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Product } from 'src/app/models/products';
import { ReviewsService } from 'src/app/services/reviews.service';
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  orders: Orders[] = [];
  user: UserProfile | null = null;
  userForm!: FormGroup;
  reviewText = '';
  selectedProductForReview?: Product;
  modalInstance?: any;
  showFeedback = false;
  feedbackMessage = '';
  rating = 0;
  hoverRating: number = 0;
  selectedProduct?: Product;
  isModalOpen = false;
  constructor(
    private orderService: OrderService,
    public authService: AuthService,
    public reviewsService: ReviewsService,
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
        this.orders.forEach((order) => {
          this.getDetailsForOrder(order.orderId);
        });
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

  getDetailsForOrder(orderId: string): void {
    this.orderService.getOrderDetails(orderId).subscribe(
      (details) => {
        console.log("Dettagli dell'ordine ricevuti:", details);
        const index = this.orders.findIndex(
          (order) => order.orderId === orderId
        );
        if (index !== -1) {
          this.orders[index] = details;
        }
      },
      (error) => {
        console.error(
          "Errore nel caricamento dei dettagli dell'ordine:",
          error
        );
      }
    );
  }

  onReviewClick(product: Product): void {
    this.selectedProductForReview = product;
    this.reviewText = '';
  }

  submitReview(): void {
    if (!this.selectedProductForReview || !this.reviewText.trim()) {
      console.error('Nessun prodotto selezionato o recensione vuota');
      return;
    }

    if (!this.authService.isAuthenticated()) {
      console.error("L'utente non è autenticato. Devi effettuare il login.");
      return;
    }

    const userId = this.authService.getCurrentUserId();
    if (!userId) {
      console.error(
        'ID utente non disponibile. Assicurati di essere autenticato correttamente.'
      );
      return;
    }

    const productId = this.selectedProductForReview.productId;

    const reviewPayload = {
      userId: userId,
      productId: productId,
      reviewText: this.reviewText,
      rating: this.rating,
    };

    this.reviewsService
      .createReview(userId, productId, reviewPayload)
      .subscribe(
        (response) => {
          console.log('Recensione inviata con successo', response);
          this.reviewText = '';
          this.showFeedback = true;
          this.feedbackMessage = 'Recensione inviata con successo!';
          setTimeout(() => {
            this.closeReviewModal();
            this.showFeedback = false;
          }, 1500);
        },
        (error) => {
          console.error('Impossibile inviare la recensione', error);
          this.feedbackMessage = "Errore durante l'invio della recensione.";
          this.showFeedback = true;
        }
      );
  }

  setRating(starValue: number): void {
    this.rating = starValue;
  }
  openReviewModal(product: Product): void {
    this.selectedProductForReview = product;
    this.isModalOpen = true;
  }

  closeReviewModal(): void {
    this.isModalOpen = false;
  }
  setHover(star: number): void {
    this.hoverRating = star;
  }

  removeHover(): void {
    this.hoverRating = 0;
  }
}
