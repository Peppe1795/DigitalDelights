import { Product } from './products';

export interface WishList {
  content: Product[]; // Sostituisci con la struttura effettiva dei dati
  totalPages: number;
  totalElements: number;
}
