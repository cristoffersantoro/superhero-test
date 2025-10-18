// src/app/app.routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [

  { path: '', pathMatch: 'full', redirectTo: 'heroes' },

  // CRUD de Heróis (lazy)
  {
    path: 'heroes',
    loadChildren: () =>
      import('./features/heroes/heroes.routes').then(m => m.default)
  },

  // opcional: rota 404
  { path: '**', redirectTo: 'heroes' }
];
