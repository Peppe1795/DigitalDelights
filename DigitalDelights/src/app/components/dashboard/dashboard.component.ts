import { Component, OnInit } from '@angular/core';
import { OrderService } from 'src/app/services/order.service';
import { Orders } from 'src/app/models/orders.interface';
import { Product } from 'src/app/models/products';
import { ProductsService } from 'src/app/services/products.service';
import { AuthService } from 'src/app/auth/auth.service';
import { UserProfile } from 'src/app/models/userprofile.interface';
import { ProductRequestPayload } from 'src/app/models/productrequestpayload.interface';

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

  constructor(
    private orderService: OrderService,
    private authSrv: AuthService,
    private productService: ProductsService
  ) {}

  ngOnInit(): void {
    this.orderService.getAllOrders().subscribe((response) => {
      this.allOrders = response.content;
    });

    this.authSrv.getAllUsers().subscribe((response: any) => {
      if (response && response.content) {
        this.allUsers = response.content;
      }
    });
    this.productService.getAllProducts().subscribe((products: Product[]) => {
      this.allProducts = products;
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

  onSubmit(formValue: any): void {
    const newProduct: ProductRequestPayload = {
      name: formValue.name,
      description: formValue.description,
      price: formValue.price,
      imageUrl: formValue.imageUrl,
      active: formValue.active || false,
      unitsInStock: formValue.unitsInStock,
      category: formValue.category,
    };

    this.productService.createProduct(newProduct).subscribe(
      (product) => {
        console.log('Prodotto creato con successo:', product);
      },
      (error) => {
        console.error('Errore nella creazione del prodotto:', error);
      }
    );
  }
}
