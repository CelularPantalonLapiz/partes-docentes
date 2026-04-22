import { Component, OnInit, ChangeDetectorRef } from "@angular/core";
import { CommonModule, Location } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { ActivatedRoute, Router, RouterModule } from "@angular/router";
import { Division } from "./disivion";
import { DivisionDat } from "./division-dat";
import { Turno } from "../turno/turno";

@Component({
  selector: "app-division-detail",
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: "./division-detail.component.html",
})
export class DivisionDetail implements OnInit {
  division: Division | null = null;
  turnos = Object.values(Turno);

  constructor(
    private divisionService: DivisionDat,
    private route: ActivatedRoute,
    private location: Location,
    private cd: ChangeDetectorRef,
    public router: Router,
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const id = params.get("id");
      if (id) {
        this.getDivision(id);
      }
    });
  }

  getDivision(id: string): void {
    if (id === "new") {
      this.division = {
        id: 0,
        anio: 0,
        numDivision: 0,
        orientacion: "",
        turno: Turno.MANANA,
      } as any;
    } else {
      this.divisionService.get(Number(id)).subscribe({
        next: (res) => {
          this.division = res.data as Division;
          this.cd.detectChanges();
        },
        error: (err) => {
          console.error("Error cargando división:", err);
          this.location.back();
        },
      });
    }
  }

  save(): void {
    if (this.division) {
      if (!this.isFormValid()) {
        alert("Por favor, completá todos los campos de la división.");
        return;
      }

      const isNew = this.route.snapshot.paramMap.get("id") === "new";
      const action = isNew
        ? this.divisionService.save(this.division)
        : this.divisionService.update(this.division);

      action.subscribe({
        next: () => this.goBack(),
        error: (err) => {
          console.error("Error al guardar división:", err);
          alert("No se pudo guardar la división. Verificá los datos.");
        },
      });
    }
  }

  goBack(): void {
    this.location.back();
  }

  isFormValid(): boolean {
    if (!this.division) return false;
    const anioOk = (this.division.anio ?? 0) > 0;
    const numOk = (this.division.numDivision ?? 0) > 0;
    const orientacionOk = (this.division.orientacion?.trim() ?? "").length > 0;
    const turnoOk = (this.division.turno?.trim() ?? "").length > 0;

    return anioOk && numOk && orientacionOk && turnoOk;
  }
}
