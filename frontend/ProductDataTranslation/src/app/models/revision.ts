import { Translation } from "./translation";

export class Revision {
    id!: number;
    version!: number;
    timestamp!: number;
    shortDescription!: string;
    longDescription!: string;
    correction!: boolean;
    translation!: Translation;
}