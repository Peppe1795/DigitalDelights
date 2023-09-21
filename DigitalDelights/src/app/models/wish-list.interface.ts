import { Product } from './products';

export interface WishList {
  content: Product[];
  totalPages: number;
  totalElements: number;
}
