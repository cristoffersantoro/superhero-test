// src/app/features/heroes/hero.model.ts
export interface Hero {
  id: number;
  nome: string;
  nomeHeroi: string;
  dataNascimento: string; // ISO instant (Z)
  altura: number;
  peso: number;
  superpoderesIds: number[];
}

export interface HeroCreateDTO {
  nome: string;
  nomeHeroi: string;
  dataNascimento: string; // ISO instant
  altura: number;
  peso: number;
  superpoderesIds: number[];
}

export interface HeroUpdateDTO extends HeroCreateDTO {}

export interface PageState {
  loading: boolean;
  saving: boolean;
  deleting: boolean;
}
