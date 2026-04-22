import { Injectable } from "@angular/core";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { Modal } from "./modal";

@Injectable({
  providedIn: "root",
})
export class ModalLogic {
  constructor(private modalService: NgbModal) {}

  confirm(title: string, message: string, description: string): Promise<any> {
    const modal = this.modalService.open(Modal);
    modal.componentInstance.title = title;
    modal.componentInstance.message = message;
    modal.componentInstance.description = description;
    return modal.result;
  }
}
