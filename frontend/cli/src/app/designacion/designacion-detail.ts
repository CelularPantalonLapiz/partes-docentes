import { Component, OnInit } from "@angular/core";
import { CommonModule, Location } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { Router, RouterModule, ActivatedRoute } from "@angular/router";
import { Designacion } from "./designacion";
import { DesignacionDat } from "./designacion-dat";
import { Persona } from "../persona/persona";
import { PersonaDat } from "../persona/persona-dat";
import { Cargo } from "../cargos/cargo";
import { CargoDat } from "../cargos/cargo-dat";

@Component({
  selector: "app-designacion-detail",
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: "./designacion-detail.component.html",
})
export class DesignacionDetail implements OnInit {
  designacion: any = {
    id: undefined,
    situacionRevista: "TITULAR",
    fechaDesde: new Date().toISOString().split("T")[0],
    fechaHasta: undefined,
    persona: undefined,
    cargo: undefined,
  };

  todasLasPersonas: Persona[] = [];
  personasFiltradas: Persona[] = [];
  busquedaPersona: string = "";
  mostrarDropdownPersona: boolean = false;

  todosLosCargos: Cargo[] = [];
  cargosFiltrados: Cargo[] = [];
  busquedaCargo: string = "";
  mostrarDropdownCargo: boolean = false;

  constructor(
    private designacionService: DesignacionDat,
    private personaService: PersonaDat,
    private cargoService: CargoDat,
    private router: Router,
    private location: Location,
    private route: ActivatedRoute,
  ) {}

  ngOnInit() {
    this.personaService
      .all()
      .subscribe((res) => (this.todasLasPersonas = res.data as Persona[]));
    this.cargoService
      .all()
      .subscribe((res) => (this.todosLosCargos = res.data as Cargo[]));

    const id = this.route.snapshot.paramMap.get("id");
    if (id && id !== "new") {
      this.designacionService.get(Number(id)).subscribe({
        next: (res) => {
          this.designacion = res.data;
          if (this.designacion.persona) {
            this.busquedaPersona = `${this.designacion.persona.dni} - ${this.designacion.persona.apellido}`;
          }
          if (this.designacion.cargo) {
            this.busquedaCargo = this.designacion.cargo.nombre;
          }
        },
        error: () => this.location.back(),
      });
    }
  }

  filtrarPersonas() {
    this.mostrarDropdownPersona = true;
    const b = this.busquedaPersona.toLowerCase();
    this.personasFiltradas = this.todasLasPersonas.filter(
      (p) =>
        p.dni.toString().includes(b) ||
        p.apellido.toLowerCase().includes(b) ||
        p.nombre.toLowerCase().includes(b),
    );
  }

  seleccionarPersona(p: Persona) {
    this.designacion.persona = p;
    this.busquedaPersona = `${p.dni} - ${p.apellido}, ${p.nombre}`;
    this.mostrarDropdownPersona = false;
  }

  filtrarCargos() {
    this.mostrarDropdownCargo = true;
    const b = this.busquedaCargo.toLowerCase();
    this.cargosFiltrados = this.todosLosCargos.filter(
      (c) =>
        c.nombre.toLowerCase().includes(b) ||
        (c.division && c.division.numDivision.toString().includes(b)),
    );
  }

  seleccionarCargo(c: Cargo) {
    this.designacion.cargo = c;
    this.busquedaCargo = c.nombre;
    this.mostrarDropdownCargo = false;
  }

  guardar() {
    if (!this.isFormValid()) return;

    const payload: any = {
      id: this.designacion.id,
      situacionRevista: this.designacion.situacionRevista,
      fechaDesde: this.designacion.fechaDesde,
      fechaHasta: this.designacion.fechaHasta || null,
      persona: {
        dni: this.designacion.persona.dni,
      },
      cargo: {
        id: this.designacion.cargo.id,
      },
    };

    console.log("Enviando designación:", payload);

    const op = this.designacion.id
      ? this.designacionService.update(payload)
      : this.designacionService.save(payload);

    op.subscribe({
      next: () => this.router.navigate(["/designaciones"]),
      error: (err) =>
        alert("Error: " + (err.error?.message || "No se pudo actualizar")),
    });
  }

  isFormValid(): boolean {
    const camposBasicosOk =
      !!this.designacion.persona &&
      !!this.designacion.cargo &&
      !!this.designacion.fechaDesde;

    if (!camposBasicosOk) return false;

    const fechasConsistentes =
      !this.designacion.fechaHasta ||
      this.designacion.fechaDesde <= this.designacion.fechaHasta;
    const vigenciaCargoOk = this.isVigenciaValida();
    return fechasConsistentes && vigenciaCargoOk;
  }

  isVigenciaValida(): boolean {
    if (!this.designacion.cargo || !this.designacion.fechaDesde) return true;

    const cargo = this.designacion.cargo;

    const inicioOk = this.designacion.fechaDesde >= cargo.fechaDesde;

    let finOk = true;
    if (cargo.fechaHasta) {
      const inicioDentro = this.designacion.fechaDesde <= cargo.fechaHasta;

      const finDentro =
        !this.designacion.fechaHasta ||
        this.designacion.fechaHasta <= cargo.fechaHasta;

      finOk = inicioDentro && finDentro;
    }

    return inicioOk && finOk;
  }
}
