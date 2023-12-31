import { ShippingInfo } from './shipping-info.interface';
import { UserProfile } from './userprofile.interface';
import { Product } from './products';
export interface Orders {
  orderId: string;
  user?: UserProfile;
  orderItems?: any[];
  status: string;
  orderDate?: string;
  totalPrice: number;
  shippingInfo: ShippingInfo;
  products: Product[];
}
