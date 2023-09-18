import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductsService } from 'src/app/services/products.service';
import { ReviewsService } from 'src/app/services/reviews.service';
import { Product } from 'src/app/models/products';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss'],
})
export class DetailsComponent implements OnInit {
  productId?: string;
  product?: Product;
  reviews: any[] = []; // <-- Aggiunta questa riga

  constructor(
    private route: ActivatedRoute,
    private productService: ProductsService,
    private reviewsService: ReviewsService // <-- Aggiunta questa riga
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.productId = id;

      // Get product details
      this.productService.getProductById(id).subscribe(
        (data) => {
          this.product = data;
        },
        (error) => {
          console.error('Errore nel recupero del prodotto', error);
        }
      );

      // Get reviews for the product
      this.reviewsService.getReviewsByProductId(id).subscribe(
        (data) => {
          this.reviews = data;
        },
        (error) => {
          console.error('Errore nel recupero delle recensioni', error);
        }
      );
    }
  }
}
