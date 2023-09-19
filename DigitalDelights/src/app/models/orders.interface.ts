import { ShippingInfo } from './shipping-info.interface';
import { UserProfile } from './userprofile.interface';
export interface Orders {
  orderId: string;
  user: UserProfile;
  orderItems: any[];
  status: string;
  orderDate: string;
  totalPrice: number;
  shippingInfo: ShippingInfo;
}
