import { Component } from "@angular/core";
import { RouterLink, RouterOutlet, Router } from "@angular/router";
import { CommonModule } from "@angular/common";

@Component({
  selector: "app-root",
  standalone: true,
  imports: [RouterOutlet, RouterLink, CommonModule],
  templateUrl: "./estructura/app.component.html",
  styleUrl: "./estructura/app.component.css",
})
export class AppComponent {
  title = "Partes docentes";

  constructor(public router: Router) {}
}
