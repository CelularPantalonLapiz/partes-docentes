import { Division } from "../division/disivion";
import { Horario } from "../horarios/horarios";
import { TipoDesignacion } from "../tipo-designacion/tipo-designacion";

export interface Cargo {
  id?: number;
  nombre: string;
  cargaHoraria: number;
  fechaDesde: string;
  fechaHasta?: string;

  tipoDesignacion: TipoDesignacion;
  division?: Division;
  horarios?: Horario[];
}
