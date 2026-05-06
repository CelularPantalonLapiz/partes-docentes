import { Component, OnInit, ChangeDetectorRef } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { Division } from "./disivion";
import { DivisionDat } from "./division-dat";
import { ResultsPage } from "../results-page";
import { Pagination } from "../pagination/pagination";
import { ModalLogic } from "../modal/modal-logic";
import { CargoDat } from "../cargos/cargo-dat";

@Component({
  selector: "app-division-list",
  standalone: true,
  imports: [CommonModule, RouterModule, Pagination],
  templateUrl: "./division-list.component.html",
})
export class DivisionList implements OnInit {
  resultsPage: ResultsPage<Division> = {
    content: [],
    number: 0,
    pageSize: 10,
    totalElements: 0,
    totalPages: 0,
    last: true,
  };
  currentPage: number = 1;

  constructor(
    private divisionService: DivisionDat,
    private cd: ChangeDetectorRef,
    private modalLogic: ModalLogic,
    private cargoService: CargoDat,
  ) {}

  ngOnInit() {
    this.getDivisions();
  }

  getDivisions(): void {
    this.divisionService
      .byPage(this.currentPage, 10)
      .subscribe((dataPackage) => {
        this.resultsPage = <ResultsPage<Division>>dataPackage.data;
        this.cd.detectChanges();
      });
  }

  remove(division: Division): void {
    // 1. Buscamos si hay cargos usando esta división
    this.cargoService.buscarPorDivision(division.id).subscribe((res) => {
      const cargosRelacionados = res.data as any[];

      if (cargosRelacionados.length > 0) {
        // 2. Si hay cargos, extraemos los IDs y mostramos el error
        const ids = cargosRelacionados.map((c) => c.id).join(", ");
        const mensajeExtra = `División: ${division.anio}° ${division.numDivision}ra ${division.turno} - ${division.orientacion}.`;

        this.modalLogic
          .confirm(
            "No se puede eliminar",
            mensajeExtra,
            `Esta división está siendo utilizada por los cargos con ID: ${ids}`,
          )
          .catch(() => {}); // El catch es para cuando cierren el modal de error
      } else {
        // 3. Si no hay cargos, procedemos con el borrado normal
        this.modalLogic
          .confirm(
            "Eliminar división",
            "¿Seguro?",
            "Esta acción no se puede deshacer.",
          )
          .then(() => {
            this.divisionService
              .remove(division.id)
              .subscribe(() => this.getDivisions());
          });
      }
    });
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getDivisions();
  }
}
