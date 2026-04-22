import { Component, OnInit, ChangeDetectorRef } from "@angular/core";
import { CommonModule, Location } from "@angular/common";
import { FormsModule } from "@angular/forms";
import { ActivatedRoute, Router, RouterModule } from "@angular/router";
import { Persona } from "./persona";
import { PersonaDat } from "./persona-dat";

@Component({
  selector: "app-persona-detail",
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: "./persona-detail.component.html",
})
export class PersonaDetail implements OnInit {
  persona: Persona | null = null;

  constructor(
    private personaService: PersonaDat,
    private route: ActivatedRoute,
    private location: Location,
    private cd: ChangeDetectorRef,
    public router: Router,
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const id = params.get("id");
      if (id) {
        this.getPersona(id);
      }
    });
  }

  getPersona(id: string): void {
    if (id === "new") {
      this.persona = {
        dni: 0,
        cuil: "",
        nombre: "",
        apellido: "",
        titulo: "",
        sexo: "Masculino",
        domicilio: "",
        telefono: "",
      } as Persona;
    } else {
      this.personaService.get(Number(id)).subscribe({
        next: (res) => {
          this.persona = res.data as Persona;
          this.cd.detectChanges();
        },
        error: (err) => {
          console.error("Error cargando persona:", err);
          this.location.back();
        },
      });
    }
  }

  save(): void {
    if (this.persona) {
      if (!this.isFormValid()) {
        alert("Por favor, completá todos los campos antes de guardar.");
        return;
      }

      const isNew = this.route.snapshot.paramMap.get("id") === "new";
      const action = isNew
        ? this.personaService.save(this.persona)
        : this.personaService.update(this.persona);

      action.subscribe({
        next: () => this.goBack(),
        error: (err) => {
          console.error("Error al guardar:", err);
          alert(
            "Hubo un error al guardar los datos. Verificá que el DNI no esté duplicado.",
          );
        },
      });
    }
  }

  goBack(): void {
    this.location.back();
  }

  isFormValid(): boolean {
    if (!this.persona) return false;

    return (
      this.persona.dni > 0 &&
      this.persona.nombre.trim().length > 0 &&
      this.persona.apellido.trim().length > 0 &&
      this.persona.cuil.trim().length > 0 &&
      this.persona.sexo.trim().length > 0 &&
      this.persona.titulo.trim().length > 0 &&
      this.persona.domicilio.trim().length > 0 &&
      this.persona.telefono.trim().length > 0
    );
  }
}
