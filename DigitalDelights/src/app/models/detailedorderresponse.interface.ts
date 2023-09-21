import { ShippingInfo } from './shipping-info.interface';
import { Product } from './products';
import { Status } from '../enum/status.enum';

export interface DetailedOrderResponse {
  orderId: string;
  status: Status;
  totalPrice: number;
  shippingInfo: ShippingInfo;
  products: Product[];
}
