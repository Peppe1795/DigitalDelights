import { Address } from '../auth/data.interface';
import { Orders } from './orders.interface';
import { Product } from './products';

export interface UserProfile {
  userId: string;
  name: string;
  username: string;
  lastName: string;
  email: string;
  address: Address;
  role: string;
  order: Orders[];
  favoriteProducts: Product[];
  reviews: any[];
}
