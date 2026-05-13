import { Component, OnInit, ChangeDetectorRef } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { Persona } from "./persona";
import { PersonaDat } from "./persona-dat";
import { ResultsPage } from "../results-page";
import { Pagination } from "../pagination/pagination";
import { ModalLogic } from "../modal/modal-logic";
import { DesignacionDat } from "../designacion/designacion-dat";

@Component({
  selector: "app-persona-list",
  standalone: true,
  imports: [CommonModule, RouterModule, Pagination],
  templateUrl: "./persona-list.component.html",
})
export class PersonaList implements OnInit {
  resultsPage: ResultsPage<Persona> = {
    content: [],
    number: 0,
    pageSize: 10,
    totalElements: 0,
    totalPages: 0,
    last: true,
  };
  currentPage: number = 1;

  constructor(
    private personaService: PersonaDat,
    private cd: ChangeDetectorRef,
    private modalLogic: ModalLogic,
    private designacionDat: DesignacionDat,
  ) {}

  ngOnInit() {
    this.getPersonas();
  }

  getPersonas(): void {
    this.personaService
      .byPage(this.currentPage, 10)
      .subscribe((dataPackage) => {
        this.resultsPage = <ResultsPage<Persona>>dataPackage.data;
        this.cd.detectChanges();
      });
  }

  remove(p: Persona): void {
    this.designacionDat.all().subscribe((pkg) => {
      const designaciones = pkg.data as any[];
      const usos = designaciones
        .filter((d) => d.persona.dni === p.dni)
        .map((d) => `Designación: ${d.cargo.nombre} (${d.fechaDesde})`);

      if (usos.length > 0) {
        this.modalLogic
          .confirm(
            "No se puede eliminar",
            `El docente ${p.apellido} no puede ser borrado porque está siendo usado en:`,
            "Debe eliminar estas designaciones antes de borrar al docente.",
            usos,
          )
          .catch(() => {});
      } else {
        this.modalLogic
          .confirm(
            "Eliminar docente",
            `¿Está seguro de eliminar a ${p.apellido}, ${p.nombre}?`,
            "Esta acción no se puede deshacer.",
          )
          .then(() => {
            this.personaService
              .remove(p.dni)
              .subscribe(() => this.getPersonas());
          })
          .catch(() => {});
      }
    });
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getPersonas();
  }
}
