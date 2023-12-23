export interface Revision {
    id: number;
    version: number;
    timestamp: number;
    shortDescription: string;
    longDescription: string;
    correction: boolean;
}