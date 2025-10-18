// src/app/features/superpowers/superpower.model.ts
export interface Superpower {
  id: number;
  nome: string;
  descricao?: string | null;
}

export interface SuperpowerCreateDTO {
  nome: string;
  descricao?: string | null;
}

export interface SuperpowerUpdateDTO extends SuperpowerCreateDTO {}
