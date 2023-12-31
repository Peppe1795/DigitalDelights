import { Component, OnInit } from '@angular/core';
import { OrderService } from 'src/app/services/order.service';
import { Orders } from 'src/app/models/orders.interface';
import { Product } from 'src/app/models/products';
import { ProductsService } from 'src/app/services/products.service';
import { AuthService } from 'src/app/auth/auth.service';
import { UserProfile } from 'src/app/models/userprofile.interface';
import { ProductRequestPayload } from 'src/app/models/productrequestpayload.interface';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit {
  selectedSection: string = '';
  allOrders: Orders[] = [];
  allUsers: UserProfile[] | null = null;
  allProducts: Product[] = [];
  currentPage: number = 1;
  totalPages: number = 1;
  productForm: FormGroup = this.fb.group({
    name: ['', Validators.required],
    description: ['', Validators.required],
    price: ['', [Validators.required]],
    imageUrl: [''],
    active: [false],
    unitsInStock: ['', Validators.required],
    category: ['', Validators.required],
  });

  constructor(
    private orderService: OrderService,
    private authSrv: AuthService,
    private productService: ProductsService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.loadProducts();
    this.orderService.getAllOrders().subscribe((response) => {
      this.allOrders = response.content;
    });

    this.authSrv.getAllUsers().subscribe((response: any) => {
      if (response && response.content) {
        this.allUsers = response.content;
      }
    });
    this.productService.getAllProducts().subscribe((response: any) => {
      if (response && response.products) {
        this.allProducts = response.products;
      } else {
        console.error('Unexpected structure of response', response);
      }
    });
  }

  navigateTo(section: string): void {
    this.selectedSection = section;
  }

  deleteOrder(orderId: string): void {
    if (window.confirm('Sei sicuro di voler cancellare questo ordine?')) {
      this.orderService.deleteOrder(orderId).subscribe(
        () => {
          this.allOrders = this.allOrders.filter(
            (order) => order.orderId !== orderId
          );
          window.alert('Ordine cancellato con successo.');
        },
        (error) => {
          console.error("Errore durante l'eliminazione dell'ordine:", error);
          window.alert(
            "Si è verificato un errore durante l'eliminazione dell'ordine."
          );
        }
      );
    }
  }

  deleteUser(userId: string): void {
    if (window.confirm('Sei sicuro di voler rimuovere questo utente?')) {
      this.authSrv.deleteUser(userId).subscribe(
        () => {
          if (this.allUsers) {
            this.allUsers = this.allUsers.filter(
              (user) => user.userId !== userId
            );
          }
          window.alert('Utente rimosso con successo.');
        },
        (error) => {
          console.error("Errore durante la rimozione dell'utente:", error);
          window.alert(
            "Si è verificato un errore durante la rimozione dell'utente."
          );
        }
      );
    }
  }

  deleteProduct(productId: string): void {
    if (window.confirm('Sei sicuro di voler rimuovere questo prodotto?')) {
      this.productService.deleteProduct(productId).subscribe(
        () => {
          this.allProducts = this.allProducts.filter(
            (product) => product.productId !== productId
          );
          window.alert('Prodotto rimosso con successo.');
        },
        (error) => {
          console.error('Errore durante la rimozione del prodotto:', error);
          window.alert(
            'Si è verificato un errore durante la rimozione del prodotto.'
          );
        }
      );
    }
  }

  onSubmit(): void {
    if (this.productForm.valid) {
      const newProduct: ProductRequestPayload = this.productForm.value;

      this.productService.createProduct(newProduct).subscribe(
        (product) => {
          this.allProducts.push(product);
          this.productForm.reset();

          window.alert('Prodotto aggiunto con successo!');
        },
        (error) => {
          console.error('Errore nella creazione del prodotto:', error);
        }
      );
    } else {
      this.productForm.markAllAsTouched();
    }
  }

  goToNextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.loadProducts();
    }
  }

  goToPreviousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.loadProducts();
    }
  }

  loadProducts(): void {
    this.productService
      .getAllProducts(this.currentPage)
      .subscribe((response: any) => {
        if (
          response &&
          response.products &&
          typeof response.totalPages === 'number'
        ) {
          this.allProducts = response.products;
          this.totalPages = response.totalPages;

          if (typeof response.currentPage === 'undefined') {
          } else {
            this.currentPage = response.currentPage + 1;
          }
        } else {
          console.error('Unexpected structure of response', response);
        }
      });
  }

  shipOrder(orderId: string): void {
    if (window.confirm('Sei sicuro di voler spedire questo ordine?')) {
      this.orderService.shipOrder(orderId).subscribe(
        (updatedOrder) => {
          const orderIndex = this.allOrders.findIndex(
            (order) => order.orderId === orderId
          );
          if (orderIndex > -1) {
            this.allOrders[orderIndex] = updatedOrder;
          }
          window.alert('Ordine spedito e email inviata.');
        },
        (error) => {
          console.error("Errore durante la spedizione dell'ordine:", error);
          window.alert(
            "Si è verificato un errore durante la spedizione dell'ordine."
          );
        }
      );
    }
  }
}
