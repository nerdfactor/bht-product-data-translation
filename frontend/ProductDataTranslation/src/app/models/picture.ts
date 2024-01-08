import { Product } from "./product";

export class Picture {
    id!: number;
    filename!: string;
    format!: string;
    height!: number;
    width!: number;
    product!: Product;
}