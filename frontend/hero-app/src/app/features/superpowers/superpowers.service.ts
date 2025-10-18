// src/app/features/superpowers/superpowers.service.ts
import { inject, Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Superpower, SuperpowerCreateDTO, SuperpowerUpdateDTO } from "./superpower.model";

const API_BASE = (typeof window === "undefined")
  ? process.env["NG_APP_API_BASE"] ?? "http://localhost:8080"
  : (window as any)["NG_APP_API_BASE"] ?? "http://localhost:8080";

@Injectable({ providedIn: "root" })
export class SuperpowersService {
  private http = inject(HttpClient);
  private baseUrl = `${API_BASE}/superpoderes`;

  list(): Observable<Superpower[]> {
    return this.http.get<Superpower[]>(this.baseUrl);
  }

  get(id: number): Observable<Superpower> {
    return this.http.get<Superpower>(`${this.baseUrl}/${id}`);
  }

  create(dto: SuperpowerCreateDTO): Observable<Superpower> {
    return this.http.post<Superpower>(this.baseUrl, dto);
  }

  update(id: number, dto: SuperpowerUpdateDTO): Observable<Superpower> {
    return this.http.put<Superpower>(`${this.baseUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
