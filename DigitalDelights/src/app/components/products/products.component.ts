import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router'; // Importiamo ActivatedRoute per accedere ai parametri dell'URL
import { Product } from 'src/app/models/products';
import { ProductsService } from 'src/app/services/products.service';
import { Category } from 'src/app/enum/category';
import { ReviewsService } from 'src/app/services/reviews.service';
import { AuthService } from 'src/app/auth/auth.service';
import { CartService } from 'src/app/services/cart.service';
import { Observable } from 'rxjs';
import { ChangeDetectorRef } from '@angular/core';
import { switchMap } from 'rxjs/operators';

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
  selectedCategory?: Category;
  reviewText = '';
  selectedProductForReview?: Product;
  modalInstance?: any;
  selectedProduct?: Product;
  rating = 0;
  hoverRating: number = 0;
  favoriteProductIds: string[] = [];
  showFeedback = false; // Mostra o nasconde il feedback
  feedbackMessage = ''; // Messaggio di feedback da mostrare all'utente

  constructor(
    private productSrv: ProductsService,
    private route: ActivatedRoute,
    public authService: AuthService,
    public reviewsService: ReviewsService,
    private cartService: CartService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadFavorites();
    console.log('Trying to reload favorites...');
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
  loadFavorites(): void {
    this.productSrv.getFavorites().subscribe(
      (response: any) => {
        this.favoriteProductIds = response.content.map(
          (product: any) => product.productId
        );
        console.log('Favorites loaded:', this.favoriteProductIds);
      },
      (error) => {
        console.error('Errore nel caricamento dei prodotti preferiti:', error);
      }
    );
  }

  addToCart(product: Product): void {
    if (!product) {
      console.error('Nessun prodotto fornito.');
      return;
    }

    this.cartService.addProductToCart(product.productId, 1).subscribe(
      () => {
        console.log('Prodotto aggiunto al carrello con successo!');
        // Aggiorna il carrello o mostra una notifica, a tua scelta.
      },
      (error) => {
        console.error(
          'Errore:',
          error.message || "Errore nell'aggiunta del prodotto al carrello."
        );
      }
    );
  }

  setHover(star: number): void {
    // Aggiunto
    this.hoverRating = star;
  }

  removeHover(): void {
    // Aggiunto
    this.hoverRating = 0;
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
      productId: productId,
      reviewText: this.reviewText,
      rating: this.rating, // aggiunge il punteggio delle stelle
    };

    // Chiamata al servizio delle recensioni con l'ID del prodotto
    this.reviewsService.createReview(productId, reviewPayload).subscribe(
      (response) => {
        console.log('Recensione inviata con successo', response);
        this.reviewText = '';
        this.showFeedback = true; // Mostra il feedback
        this.feedbackMessage = 'Recensione inviata con successo!'; // Imposta il messaggio di feedback
        setTimeout(() => {
          this.closeReviewModal(); // Chiudi la modale dopo un breve ritardo per permettere all'utente di leggere il feedback
          this.showFeedback = false; // Nascondi il feedback dopo la chiusura della modale
        }, 1500);
      },
      (error) => {
        console.error('Impossibile inviare la recensione', error);
        this.feedbackMessage = "Errore durante l'invio della recensione."; // Imposta un messaggio di feedback per errori
        this.showFeedback = true; // Mostra il feedback
      }
    );
  }

  setRating(starValue: number): void {
    this.rating = starValue;
  }
  openReviewModal(product: Product): void {
    this.selectedProduct = product;
    this.selectedProductForReview = product;
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
  toggleFavorite(product: Product): void {
    if (!product) {
      console.error('Nessun prodotto fornito.');
      return;
    }

    if (!this.authService.isAuthenticated()) {
      console.error("L'utente non è autenticato. Devi effettuare il login.");
      return;
    }

    let actionObservable: Observable<any>;

    if (this.favoriteProductIds.includes(product.productId)) {
      actionObservable = this.productSrv.removeFromFavorites(product.productId);
    } else {
      actionObservable = this.productSrv.addToFavorites(product.productId);
    }

    actionObservable.subscribe(
      () => {
        if (this.favoriteProductIds.includes(product.productId)) {
          // Se l'azione era una rimozione, rimuovi l'ID del prodotto dai preferiti
          this.favoriteProductIds = this.favoriteProductIds.filter(
            (id) => id !== product.productId
          );
        } else {
          // Altrimenti, se l'azione era un'aggiunta, aggiungi l'ID del prodotto ai preferiti
          this.favoriteProductIds.push(product.productId);
        }
        console.log('Prodotti preferiti aggiornati:', this.favoriteProductIds);
        this.cdr.detectChanges();
      },
      (error) => {
        console.error(
          'Errore:',
          error.message || "Errore nell'aggiornamento dei prodotti preferiti."
        );
      }
    );
  }
}
