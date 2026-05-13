import { Component, OnInit } from "@angular/core";
import { CommonModule, DatePipe } from "@angular/common";
import { RouterModule } from "@angular/router";
import { DesignacionDat } from "./designacion-dat";
import { Designacion } from "./designacion";
import { ResultsPage } from "../results-page";
import { ModalLogic } from "../modal/modal-logic";
import { Pagination } from "../pagination/pagination";

@Component({
  selector: "app-designacion-list",
  standalone: true,
  imports: [CommonModule, RouterModule, DatePipe, Pagination],
  templateUrl: "./designacion-list.component.html",
})
export class DesignacionListComponent implements OnInit {
  resultsPage: ResultsPage<Designacion> = {
    content: [],
    number: 0,
    pageSize: 10,
    totalElements: 0,
    totalPages: 0,
    last: true,
  };
  currentPage: number = 1;

  constructor(
    private designacionDat: DesignacionDat,
    private modalLogic: ModalLogic,
  ) {}

  ngOnInit(): void {
    this.listar();
  }

  listar(): void {
    this.designacionDat.byPage(this.currentPage, 10).subscribe((pkg) => {
      this.resultsPage = <ResultsPage<Designacion>>pkg.data;
    });
  }

  verPersona(p: any): void {
    const info = `Nombre: ${p.nombre} ${p.apellido}\nTítulo: ${p.titulo || "No posee"}`;
    this.modalLogic.confirm(`Datos de la Persona (DNI: ${p.dni})`, info, "");
  }

  borrar(id: number): void {
    this.modalLogic
      .confirm(
        "Eliminar Designación",
        "¿Seguro?",
        "Esta acción no se puede deshacer.",
      )
      .then(() => {
        this.designacionDat.remove(id).subscribe({
          next: () => this.listar(),
          error: (err) => console.error("Error al borrar", err),
        });
      })
      .catch(() => {});
  }

  onPageChangeRequested(page: number): void {
    this.currentPage = page;
    this.listar();
  }

  verOcupantes(d: Designacion): void {
    if (d.situacionRevista === "TITULAR") return;

    this.designacionDat.getOcupantes(d.id).subscribe((pkg) => {
      const ocupantes = pkg.data as Designacion[];
      let info = "";
      const titular = ocupantes.find((o) => o.situacionRevista === "TITULAR");
      const suplente = ocupantes.find((o) => o.situacionRevista === "SUPLENTE");

      if (d.situacionRevista === "SUPLENTE") {
        info = titular
          ? `Titular: ${titular.persona.nombre} ${titular.persona.apellido}`
          : "No se encontró el titular activo.";
      } else if (d.situacionRevista === "ERROR") {
        info = `Titular: ${titular ? titular.persona.nombre + " " + titular.persona.apellido : "N/C"}\n`;
        info += `Suplente: ${suplente ? suplente.persona.nombre + " " + suplente.persona.apellido : "N/C"}`;
      }

      this.modalLogic.confirm(`Ocupantes del Cargo`, info, "");
    });
  }
}
