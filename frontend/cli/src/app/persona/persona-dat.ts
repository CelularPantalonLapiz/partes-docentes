import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { DataPackage } from "../data-package";
import { Observable } from "rxjs";
import { Persona } from "./persona";

@Injectable({
  providedIn: "root",
})
export class PersonaDat {
  private personaUrl = "rest/personas";

  constructor(private httpClient: HttpClient) {}

  all(): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(this.personaUrl);
  }

  remove(id: number): Observable<DataPackage> {
    return this.httpClient.delete<DataPackage>(`${this.personaUrl}/${id}`);
  }

  save(persona: Persona): Observable<DataPackage> {
    return this.httpClient.post<DataPackage>(this.personaUrl, persona);
  }

  update(persona: Persona): Observable<DataPackage> {
    return this.httpClient.put<DataPackage>(this.personaUrl, persona);
  }

  get(id: number): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(`${this.personaUrl}/dni/${id}`);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(
      `${this.personaUrl}/page?page=${page - 1}&size=${size}`,
    );
  }
  search(search: string): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(
      `${this.personaUrl}/search/${search}`,
    );
  }
}
