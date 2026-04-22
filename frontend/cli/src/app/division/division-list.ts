import { Component, OnInit, ChangeDetectorRef } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { Division } from "./disivion";
import { DivisionDat } from "./division-dat";
import { ResultsPage } from "../results-page";
import { Pagination } from "../pagination/pagination";
import { ModalLogic } from "../modal/modal-logic";

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

  remove(id: number): void {
    this.modalLogic
      .confirm(
        "Eliminar división",
        "¿Seguro?",
        "Esta acción no se puede deshacer.",
      )
      .then(() => {
        this.divisionService.remove(id).subscribe(() => this.getDivisions());
      });
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.getDivisions();
  }
}
