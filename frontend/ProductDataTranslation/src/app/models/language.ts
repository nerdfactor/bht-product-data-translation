import { Translation } from "./translation";

export interface Language {
    id: number;
    name: string;
    currency: string;
    measure: string;
    isoCode: string;
    translations?: Translation[];
}