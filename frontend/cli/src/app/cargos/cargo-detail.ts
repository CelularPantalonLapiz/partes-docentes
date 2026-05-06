import { Component, OnInit } from "@angular/core";
import { CommonModule, Location } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { Router, RouterModule, ActivatedRoute } from "@angular/router";
import { Cargo } from "./cargo";
import { CargoDat } from "./cargo-dat";
import { Division } from "../division/disivion";
import { DivisionDat } from "../division/division-dat";
import { TipoDesignacion } from "../tipo-designacion/tipo-designacion";

@Component({
  selector: "app-cargo-detail",
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: "./cargo-deatil.component.html",
})
export class CargoDetail implements OnInit {
  cargo: Cargo = {
    id: undefined,
    nombre: "",
    cargaHoraria: 0,
    fechaDesde: new Date().toISOString().split("T")[0],
    fechaHasta: undefined,
    tipoDesignacion: TipoDesignacion.CARGO,
    horarios: [],
  };

  todasLasDivisiones: Division[] = [];
  divisionesFiltradas: Division[] = [];
  busquedaDivision: string = "";
  mostrarDropdown: boolean = false;

  constructor(
    private cargoService: CargoDat,
    private divisionService: DivisionDat,
    private router: Router,
    private location: Location,
    private route: ActivatedRoute,
  ) {}

  ngOnInit() {
    this.divisionService.all().subscribe((res) => {
      this.todasLasDivisiones = res.data as Division[];
    });
    const id = this.route.snapshot.paramMap.get("id");
    if (id && id !== "new") {
      this.cargoService.get(Number(id)).subscribe({
        next: (res) => {
          this.cargo = res.data as Cargo;
          if (this.cargo.division) {
            const d = this.cargo.division;
            this.busquedaDivision = `${d.anio}° ${d.numDivision}ra - ${d.orientacion}`;
          }
        },
        error: (err) => this.location.back(),
      });
    }
  }

  filtrarDivisiones() {
    this.mostrarDropdown = true;
    if (!this.busquedaDivision) {
      this.divisionesFiltradas = [];
      this.mostrarDropdown = false;
      return;
    }

    const busqueda = this.busquedaDivision.toLowerCase();
    this.divisionesFiltradas = this.todasLasDivisiones.filter(
      (d) =>
        d.anio.toString().includes(busqueda) ||
        d.numDivision.toString().includes(busqueda) ||
        d.orientacion.toLowerCase().includes(busqueda),
    );
  }

  seleccionarDivision(d: Division) {
    this.cargo.division = d;
    this.busquedaDivision = `${d.anio}° ${d.numDivision}ra - ${d.orientacion}`;
    this.mostrarDropdown = false;
  }

  guardar() {
    if (!this.isFormValid()) {
      return;
    }

    const esCargoInstitucional =
      this.cargo.tipoDesignacion === TipoDesignacion.CARGO;

    const cargoParaGuardar: any = {
      id: this.cargo.id,
      nombre: this.cargo.nombre,
      cargaHoraria: this.cargo.cargaHoraria,
      tipoDesignacion: this.cargo.tipoDesignacion,
      fechaDesde: this.cargo.fechaDesde
        ? this.cargo.fechaDesde.split("T")[0]
        : null,
      fechaHasta: this.cargo.fechaHasta
        ? this.cargo.fechaHasta.split("T")[0]
        : null,
      horarios: [],
      division:
        !esCargoInstitucional && this.cargo.division
          ? { id: this.cargo.division.id }
          : null,
    };

    console.log("Enviando cargo:", cargoParaGuardar);
    const operacion = this.cargo.id
      ? this.cargoService.update(cargoParaGuardar)
      : this.cargoService.save(cargoParaGuardar);

    operacion.subscribe({
      next: () => {
        console.log("¡Éxito!");
        this.router.navigate(["/cargos"]);
      },
      error: (err) => {
        console.error("Error en el servidor:", err);
        alert("No se pudo guardar el cargo. Revisá los logs de Docker.");
      },
    });
  }

  isFormValid(): boolean {
    const esCargoInst = this.cargo.tipoDesignacion === TipoDesignacion.CARGO;
    const nombreOk = this.cargo.nombre.trim().length > 0;
    const cargaOk = this.cargo.cargaHoraria > 0;
    const fechaOk = !!this.cargo.fechaDesde;
    const divisionOk = esCargoInst || !!this.cargo.division;
    return nombreOk && cargaOk && fechaOk && divisionOk;
  }
}
