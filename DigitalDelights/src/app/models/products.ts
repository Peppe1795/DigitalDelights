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
  category: ProductCategory;
  reviews: string[]; // Supponendo che sia una lista di stringhe
}
export enum ProductCategory {
  SMARTPHONE = 'SMARTPHONE',
  // Aggiungi altre categorie qui se necessario
}
