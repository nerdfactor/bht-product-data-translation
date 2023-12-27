import { Language } from "./language";
import { Product } from "./product";

export interface Translation {
    id: number;
    shortDescription: string;
    longDescription: string;
    product?: Product;
    language?: Language;
}