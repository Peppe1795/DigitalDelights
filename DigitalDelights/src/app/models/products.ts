import { Category } from '../enum/category';
export interface ApiResponse {
  content: Product[];
}
export interface Product {
  productId: string;
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  active: boolean;
  unitsInStock: number;
  dateCreated: Date;
  lastUpdated: Date;
  category: Category;
  reviews: string[]; // Supponendo che sia una lista di stringhe
}
