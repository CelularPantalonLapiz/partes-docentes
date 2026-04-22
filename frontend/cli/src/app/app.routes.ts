import { Routes } from "@angular/router";
import { Home } from "./home/home";
import { PersonaList } from "./persona/persona-list";
import { PersonaDetail } from "./persona/persona-detail";
import { DivisionList } from "./division/division-list";
import { DivisionDetail } from "./division/division-detail";
import { CargoListComponent } from "./cargos/cargo-list";
import { CargoDetail } from "./cargos/cargo-detail";

export const routes: Routes = [
  { path: "", component: Home },

  { path: "personas", component: PersonaList },
  { path: "personas/:id", component: PersonaDetail },
  { path: "divisiones", component: DivisionList },
  { path: "divisiones/:id", component: DivisionDetail },
  { path: "cargos", component: CargoListComponent },
  { path: "cargos/new", component: CargoDetail },
  { path: "**", redirectTo: "", pathMatch: "full" },
];
