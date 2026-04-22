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
        aria-describedby="modal-title"
        (click)="modal.dismiss()"
      ></button>
    </div>
    <div class="modal-body">
      <p>
        <strong>{{ message }}</strong>
      </p>
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
        Cancelar
      </button>
      <button type="button" class="btn btn-danger" (click)="modal.close()">
        Aceptar
      </button>
    </div>
  `,
})
export class Modal {
  title = "";
  message = "";
  description = "";
  constructor(public modal: NgbActiveModal) {}
}
