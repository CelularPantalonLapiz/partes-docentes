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
    this.cargoService.buscarPorDivision(division.id).subscribe((res) => {
      const cargosRelacionados = res.data as any[];

      if (cargosRelacionados.length > 0) {
        const usos = cargosRelacionados.map(
          (c) => `Cargo: ${c.nombre} (ID: ${c.id})`,
        );
        this.modalLogic
          .confirm(
            "No se puede eliminar",
            `La división ${division.anio}° ${division.numDivision}ra turno ${division.turno} no puede ser borrada porque está asignada a:`,
            "Debe eliminar o reasignar estos cargos antes de borrar la división.",
            usos,
          )
          .catch(() => {});
      } else {
        this.modalLogic
          .confirm(
            "Eliminar división",
            `¿Está seguro de eliminar la división ${division.anio}° ${division.numDivision}ra?`,
            "Esta acción no se puede deshacer.",
          )
          .then(() => {
            this.divisionService
              .remove(division.id)
              .subscribe(() => this.getDivisions());
          })
          .catch(() => {});
      }
    });
  }
  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getDivisions();
  }
}
