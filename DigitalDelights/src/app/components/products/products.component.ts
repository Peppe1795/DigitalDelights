import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router'; // Importiamo ActivatedRoute per accedere ai parametri dell'URL
import { Product } from 'src/app/models/products';
import { ProductsService } from 'src/app/services/products.service';
import { Category } from 'src/app/enum/category';
import { ReviewsService } from 'src/app/services/reviews.service';
import { AuthService } from 'src/app/auth/auth.service';

declare var bootstrap: any;
@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent implements OnInit {
  categoriesWithProducts: { [key: string]: Product[] } = {};
  objectKeys = Object.keys;
  loading = true;
  selectedCategory?: Category; // Aggiunto per gestire la categoria selezionata
  reviewText = '';
  selectedProductForReview?: Product;
  modalInstance?: any;
  selectedProduct?: Product;

  constructor(
    private productSrv: ProductsService,
    private route: ActivatedRoute, // Injectiamo ActivatedRoute nel costruttore
    public authService: AuthService,
    public reviewsService: ReviewsService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      if (params['category']) {
        this.selectedCategory = params['category'] as Category;
        this.loadProductsBySelectedCategory();
      } else {
        this.loadAllProductsByCategories();
      }
    });
  }
  getObjectKeys(obj: any): string[] {
    return Object.keys(obj);
  }
  loadAllProductsByCategories(): void {
    const categories = Object.values(Category);

    let completedRequests = 0;

    categories.forEach((category) => {
      this.productSrv.getProductsByCategory(category).subscribe(
        (response) => {
          this.categoriesWithProducts[category] = response.content;

          completedRequests++;
          if (completedRequests === categories.length) {
            this.loading = false;
          }
        },
        (error) => {
          console.error('Error fetching products by category:', error);
          this.loading = false;
        }
      );
    });
  }

  loadProductsBySelectedCategory(): void {
    if (!this.selectedCategory) return;

    this.productSrv.getProductsByCategory(this.selectedCategory).subscribe(
      (response) => {
        this.categoriesWithProducts = {};
        if (this.selectedCategory) {
          // Questo controllo è superfluo, ma serve per soddisfare TypeScript
          this.categoriesWithProducts[this.selectedCategory] = response.content;
        }
        this.loading = false;
      },
      (error) => {
        console.error('Error fetching products by selected category:', error);
        this.loading = false;
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
    const productId = this.selectedProductForReview.productId; // Ottieni l'ID del prodotto selezionato

    const reviewPayload = {
      userId: userId,
      productId: productId, // Utilizza l'ID del prodotto selezionato
      reviewText: this.reviewText,
    };

    // Chiamata al servizio delle recensioni con l'ID del prodotto
    this.reviewsService.createReview(productId, reviewPayload).subscribe(
      (response) => {
        console.log('Recensione inviata con successo', response);
        this.reviewText = '';
      },
      (error) => {
        console.error('Impossibile inviare la recensione', error);
      }
    );
  }

  openReviewModal(product: Product): void {
    this.selectedProduct = product;
    console.log('Is Authenticated:', this.authService.isAuthenticated()); // verifica lo stato dell'autenticazione
    console.log('Selected Product for Review:', this.selectedProductForReview); // verifica il prodotto selezionato
    const modalElement = document.getElementById('reviewModal');
    this.modalInstance = new bootstrap.Modal(modalElement);
    this.modalInstance.show();
  }
  closeReviewModal(): void {
    if (this.modalInstance) {
      this.modalInstance.hide();
    }
  }
}
