import { Component } from "@angular/core";

@Component({
  selector: "app-home",
  standalone: true,
  template: `
    <div
      class="d-flex flex-column align-items-center justify-content-center"
      style="min-height: 50vh;"
    >
      <h1 class="display-3 fw-bold text-center">Bienvenido a la Escuela 775</h1>
      <h2 class="text-muted text-center">
        Sistema de Gestión de Partes Docentes
      </h2>
    </div>
  `,
})
export class Home {}
