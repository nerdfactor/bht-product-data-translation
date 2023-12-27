import { Product } from "./product";

export interface Color {
    id: number;
    name: string;
    products: Product[];
}