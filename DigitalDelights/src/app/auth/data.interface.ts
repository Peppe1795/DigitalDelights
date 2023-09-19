import { Product } from '../models/products';
import { Orders } from '../models/orders.interface';

export interface Data {
  accessToken: string;
  user: {
    userId: string;
    username: string;
    name: string;
    lastName: string;
    email: string;
    address: Address;
    role: string;
    order: Orders[];
    favoriteProducts: Product[];
  };
}

export interface Address {
  via: string;
  numeroCivico: number;
  localita: string;
  cap: string;
  comune: string;
}
