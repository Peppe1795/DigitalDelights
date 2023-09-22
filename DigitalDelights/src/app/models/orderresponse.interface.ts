import { Orders } from './orders.interface';
export interface Pageable {
  pageNumber: number;
  pageSize: number;
  offset: number;
}

export interface OrderResponse {
  content: Orders[];
  pageable: Pageable;
}
