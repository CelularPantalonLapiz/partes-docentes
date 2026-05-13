import { Component, OnInit } from "@angular/core";
import { CommonModule, DatePipe } from "@angular/common";
import { RouterModule } from "@angular/router";
import { Cargo } from "./cargo";
import { CargoDat } from "./cargo-dat";
import { ResultsPage } from "../results-page";
import { Pagination } from "../pagination/pagination";
import { ModalLogic } from "../modal/modal-logic";
import { DesignacionDat } from "../designacion/designacion-dat";

@Component({
  selector: "app-cargo-list",
  standalone: true,
  imports: [CommonModule, DatePipe, RouterModule, Pagination],
  templateUrl: "./cargo-list.component.html",
})
export class CargoListComponent implements OnInit {
  resultsPage: ResultsPage<Cargo> = {
    content: [],
    number: 0,
    pageSize: 10,
    totalElements: 0,
    totalPages: 0,
    last: true,
  };
  currentPage: number = 1;

  constructor(
    private cargoDat: CargoDat,
    private modalLogic: ModalLogic,
    private designacionesDat: DesignacionDat,
  ) {}

  ngOnInit(): void {
    this.listarCargos();
  }

  listarCargos(): void {
    this.cargoDat.byPage(this.currentPage, 10).subscribe((dataPackage) => {
      this.resultsPage = <ResultsPage<Cargo>>dataPackage.data;
    });
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.listarCargos();
  }

  borrarCargo(cargo: Cargo): void {
    this.designacionesDat.all().subscribe((pkg) => {
      const designaciones = pkg.data as any[];
      const usos = designaciones
        .filter((d) => d.cargo.id === cargo.id)
        .map(
          (d) =>
            `Designado/a: ${d.persona.apellido}, ${d.persona.nombre} (DNI: ${d.persona.dni})`,
        );

      if (usos.length > 0) {
        this.modalLogic
          .confirm(
            "No se puede eliminar",
            `El cargo "${cargo.nombre}" no puede ser borrado porque ya tiene personal designado:`,
            "Debe dar de baja estas designaciones antes de borrar el cargo.",
            usos,
          )
          .catch(() => {});
      } else {
        this.modalLogic
          .confirm(
            "Eliminar cargo",
            `¿Está seguro de eliminar el cargo "${cargo.nombre}"?`,
            "Esta acción no se puede deshacer.",
          )
          .then(() => {
            if (cargo.id) {
              this.cargoDat
                .remove(cargo.id)
                .subscribe(() => this.listarCargos());
            }
          })
          .catch(() => {});
      }
    });
  }
}
