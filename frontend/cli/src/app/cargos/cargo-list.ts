import { Component, OnInit } from "@angular/core";
import { CommonModule, DatePipe } from "@angular/common";
import { RouterModule } from "@angular/router";
import { Cargo } from "./cargo";
import { CargoDat } from "./cargo-dat";
import { ResultsPage } from "../results-page";
import { Pagination } from "../pagination/pagination";
import { ModalLogic } from "../modal/modal-logic";

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

  borrarCargo(id: number): void {
    this.modalLogic
      .confirm(
        "Eliminar cargo",
        "¿Seguro?",
        "Esta acción no se puede deshacer.",
      )
      .then(() => {
        this.cargoDat.remove(id).subscribe({
          next: () => {
            this.listarCargos();
            console.log("Cargo eliminado");
          },
          error: (err) => console.error("Error al borrar", err),
        });
      })
      .catch(() => {});
  }
}
