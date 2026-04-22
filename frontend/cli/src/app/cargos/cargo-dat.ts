import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Cargo } from "./cargo";
import { DataPackage } from "../data-package";

@Injectable({
  providedIn: "root",
})
export class CargoDat {
  private cargosUrl = "rest/cargos";

  constructor(private httpClient: HttpClient) {}

  all(): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(this.cargosUrl);
  }

  remove(id: number): Observable<DataPackage> {
    return this.httpClient.delete<DataPackage>(`${this.cargosUrl}/${id}`);
  }

  save(cargo: Cargo): Observable<DataPackage> {
    return this.httpClient.post<DataPackage>(this.cargosUrl, cargo);
  }

  update(cargo: Cargo): Observable<DataPackage> {
    return this.httpClient.put<DataPackage>(this.cargosUrl, cargo);
  }

  get(id: number): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(`${this.cargosUrl}/id/${id}`);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(
      `${this.cargosUrl}/page?page=${page - 1}&size=${size}`,
    );
  }
  search(search: string): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(
      `${this.cargosUrl}/search/${search}`,
    );
  }
}
