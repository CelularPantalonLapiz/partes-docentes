import { Component, OnInit, ChangeDetectorRef } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { Persona } from "./persona";
import { PersonaDat } from "./persona-dat";
import { ResultsPage } from "../results-page";
import { Pagination } from "../pagination/pagination";
import { ModalLogic } from "../modal/modal-logic";

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

  remove(dni: number): void {
    this.modalLogic
      .confirm("Eliminar docente", "¿Seguro?", "No se puede deshacer.")
      .then(() => {
        this.personaService.remove(dni).subscribe(() => this.getPersonas());
      });
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getPersonas();
  }
}
