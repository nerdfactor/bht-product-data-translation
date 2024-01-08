import { Category } from "./category";
import { Color } from "./color";
import { Picture } from "./picture";
import { Translation } from "./translation";

export interface Product {
    id: number;
    serial: string;
    name: string;
    height: number;
    width: number;
    depth: number;
    weight: number;
    price: number;
    categories: Category[];
    colors: Color[];
    pictures: Picture[];
    translations: Translation[];
}