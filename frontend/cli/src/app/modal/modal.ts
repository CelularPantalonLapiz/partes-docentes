import { Component } from "@angular/core";
import { CommonModule } from "@angular/common";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: "app-modal",
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="modal-header">
      <h4 class="modal-title" id="modal-title">{{ title }}</h4>
      <button
        type="button"
        class="btn-close"
        (click)="modal.dismiss()"
      ></button>
    </div>
    <div class="modal-body">
      <p>
        <strong>{{ message }}</strong>
      </p>

      <ul *ngIf="items.length > 0" class="list-group mb-3">
        <li
          *ngFor="let item of items"
          class="list-group-item list-group-item-danger py-1"
        >
          {{ item }}
        </li>
      </ul>

      <p *ngIf="description">
        <strong>{{ description }}</strong>
      </p>
    </div>
    <div class="modal-footer">
      <button
        type="button"
        class="btn btn-outline-secondary"
        (click)="modal.dismiss()"
      >
        {{ items.length > 0 ? "Cerrar" : "Cancelar" }}
      </button>
      <button
        *ngIf="items.length === 0"
        type="button"
        class="btn btn-danger"
        (click)="modal.close()"
      >
        Aceptar
      </button>
    </div>
  `,
})
export class Modal {
  title = "";
  message = "";
  description = "";
  items: string[] = [];
  constructor(public modal: NgbActiveModal) {}
}
