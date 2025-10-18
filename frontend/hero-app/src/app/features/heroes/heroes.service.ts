// src/app/features/heroes/heroes.service.ts
import { inject, Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Hero, HeroCreateDTO, HeroUpdateDTO } from "./hero.model";

// You may centralize this in environment.ts; included here for simplicity.
// Prefer to move to an environment variable: environment.apiBaseUrl
const API_BASE = (typeof window === "undefined")
  ? process.env["NG_APP_API_BASE"] ?? "http://localhost:8080"
  : (window as any)["NG_APP_API_BASE"] ?? "http://localhost:8080";

@Injectable({ providedIn: "root" })
export class HeroesService {
  private http = inject(HttpClient);
  private baseUrl = `${API_BASE}/herois`;

  list(): Observable<Hero[]> {
    return this.http.get<Hero[]>(this.baseUrl);
  }

  get(id: number): Observable<Hero> {
    return this.http.get<Hero>(`${this.baseUrl}/${id}`);
  }

  create(dto: HeroCreateDTO): Observable<Hero> {
    return this.http.post<Hero>(this.baseUrl, dto, { observe: "body" });
  }

  update(id: number, dto: HeroUpdateDTO): Observable<Hero> {
    return this.http.put<Hero>(`${this.baseUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  addPower(id: number, superpoderId: number): Observable<Hero> {
    return this.http.post<Hero>(`${this.baseUrl}/${id}/superpoderes/${superpoderId}`, {});
  }

  removePower(id: number, superpoderId: number): Observable<Hero> {
    return this.http.delete<Hero>(`${this.baseUrl}/${id}/superpoderes/${superpoderId}`);
  }
}
