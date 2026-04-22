import { Turno } from "../turno/turno";

export interface Division {
  id: number;
  anio: number;
  numDivision: number;
  turno: Turno;
  orientacion: string;
}
