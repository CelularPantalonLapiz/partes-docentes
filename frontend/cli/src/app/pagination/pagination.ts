import { CommonModule } from "@angular/common";
import {
  Component,
  EventEmitter,
  Input,
  Output,
  SimpleChanges,
} from "@angular/core";
import { ResultsPage } from "../results-page";
import { SimpleChange } from "@angular/core";

@Component({
  selector: "app-pagination",
  standalone: true,
  imports: [CommonModule],
  template: `
    <nav aria-label="Page navigation">
      <ul class="pagination pagination-centered">
        <li class="page-item">
          <a class="page-link" (click)="ongPageChange(-2)">&laquo;</a>
        </li>
        <li class="page-item">
          <a class="page-link" (click)="ongPageChange(-1)">&lsaquo;</a>
        </li>
        <li *ngFor="let t of pages" [ngClass]="t === number ? 'active' : ''">
          <a class="page-link" (click)="ongPageChange(t + 1)">{{ t + 1 }};</a>
        </li>
        <li class="page-item">
          <a class="page-link" (click)="ongPageChange(-3)">&rsaquo;</a>
        </li>
        <li class="page-item">
          <a class="page-link" (click)="ongPageChange(-4)">&raquo;</a>
        </li>
      </ul>
    </nav>
  `,
  styles: ``,
})
export class Pagination {
  @Input() totalPages: number = 0;
  @Input() currentPage: number = 1;
  @Input() last: boolean = false;
  @Input() number: number = 1;
  @Output() pageChangeResquest = new EventEmitter<number>();
  pages: number[] = [];

  contructor() {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes["totalPages"]) {
      this.pages = Array.from(Array(this.totalPages).keys());
    }
  }

  ongPageChange(pageId: number): void {
    if (!this.currentPage) {
      this.currentPage = 1;
    }
    let page = pageId;
    if (pageId === -2) {
      page = 1;
    }
    if (pageId === -1) {
      page = this.currentPage > 1 ? this.currentPage - 1 : 1;
    }
    if (pageId === -3) {
      page = !this.last ? this.currentPage + 1 : this.currentPage;
    }
    if (pageId === -4) {
      page = this.totalPages;
    }

    if (pageId > 1 && this.pages.length >= pageId) {
      page = this.pages[pageId - 1] + 1;
    }

    this.currentPage = page;
    this.pageChangeResquest.emit(page);
  }
}
