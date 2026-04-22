import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { DataPackage } from "../data-package";
import { Observable } from "rxjs";
import { Division } from "./disivion";

@Injectable({
  providedIn: "root",
})
export class DivisionDat {
  private divisionUrl = "rest/divisiones";

  constructor(private httpClient: HttpClient) {}

  all(): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(this.divisionUrl);
  }

  remove(id: number): Observable<DataPackage> {
    return this.httpClient.delete<DataPackage>(`${this.divisionUrl}/${id}`);
  }

  save(division: Division): Observable<DataPackage> {
    return this.httpClient.post<DataPackage>(this.divisionUrl, division);
  }

  update(division: Division): Observable<DataPackage> {
    return this.httpClient.put<DataPackage>(this.divisionUrl, division);
  }

  get(id: number): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(`${this.divisionUrl}/id/${id}`);
  }

  byPage(page: number, size: number): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(
      `${this.divisionUrl}/page?page=${page - 1}&size=${size}`,
    );
  }
  search(search: string): Observable<DataPackage> {
    return this.httpClient.get<DataPackage>(
      `${this.divisionUrl}/search/${search}`,
    );
  }
}
