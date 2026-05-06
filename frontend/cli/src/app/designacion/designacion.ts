import { Persona } from "../persona/persona";
import { Cargo } from "../cargos/cargo";

export interface Designacion {
  id: number;
  situacionRevista: string;
  fechaDesde: string;
  fechaHasta: string;
  persona: Persona;
  cargo: Cargo;
}
