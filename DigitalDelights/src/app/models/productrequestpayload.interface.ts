import { Category } from '../enum/category';

export interface ProductRequestPayload {
  name: string;
  description: string;
  price: number;
  imageUrl: string;
  active: boolean;
  unitsInStock: number;
  category: Category;
}
