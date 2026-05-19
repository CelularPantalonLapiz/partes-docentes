import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Designacion } from "./designacion";
import { DataPackage } from "../data-package";

@Injectable({ providedIn: "root" })
export class DesignacionDat {
  private designacionesUrl = "rest/designaciones";

  constructor(private httpClient: HttpClient) {}

  all(): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(this.designacionesUrl);
  }

  remove(id: number): Observable<DataPackage> {
    return this.httpClient.delete<DataPackage>(
      `${this.designacionesUrl}/${id}`,
    );
  }

  save(designaciones: Designacion): Observable<DataPackage> {
    return this.httpClient.post<DataPackage>(
      this.designacionesUrl,
      designaciones,
    );
  }

  update(designaciones: Designacion): Observable<DataPackage> {
    return this.httpClient.put<DataPackage>(
      this.designacionesUrl,
      designaciones,
    );
  }

  get(id: number): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(
      `${this.designacionesUrl}/id/${id}`,
    );
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(
      `${this.designacionesUrl}/page?page=${page - 1}&size=${size}`,
    );
  }
  search(search: string): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(
      `${this.designacionesUrl}/search/${search}`,
    );
  }

  getOcupantes(id: number): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(
      `${this.designacionesUrl}/${id}/ocupantes`,
    );
  }

  getErrores(): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(`${this.designacionesUrl}/errores`);
  }
}
