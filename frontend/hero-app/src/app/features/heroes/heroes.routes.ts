// src/app/features/heroes/heroes.routes.ts
import { Routes } from "@angular/router";
import { HeroesComponent } from "./heroes.component";

export default [
  { path: "", component: HeroesComponent, title: "Heróis" }
] as Routes;
