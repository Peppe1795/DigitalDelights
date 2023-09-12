import { Component, OnInit } from '@angular/core';
import { WishListService } from 'src/app/services/wish-list.service';
import { Product } from 'src/app/models/products';
import { AuthService } from 'src/app/auth/auth.service';

@Component({
  selector: 'app-wish-list',
  templateUrl: './wish-list.component.html',
  styleUrls: ['./wish-list.component.scss'],
})
export class WishListComponent implements OnInit {
  favorites: Product[] = [];
  currentPage = 0;
  pageSize = 10;

  constructor(
    private wishlistService: WishListService,
    private authSrv: AuthService
  ) {}

  ngOnInit(): void {
    const userId = this.authSrv.getCurrentUserId();
    if (userId) {
      this.loadWishlist(userId);
    } else {
      console.error(
        "L'utente non è autenticato o l'ID utente non è disponibile."
      );
    }
  }

  loadWishlist(userId: string): void {
    this.wishlistService
      .getUserWishlist(userId, this.currentPage, this.pageSize)
      .subscribe(
        (response: any) => {
          this.favorites = response.content;
        },
        (error) => {
          console.error('Errore nel recupero dei preferiti:', error);
        }
      );
  }

  loadMoreProducts(): void {
    // Incrementa la pagina corrente prima di caricare più prodotti
    this.currentPage++;
    const userId = this.authSrv.getCurrentUserId();
    if (userId) {
      this.loadWishlist(userId);
    }
  }
}
