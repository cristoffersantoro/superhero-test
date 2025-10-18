// src/app/features/heroes/api-error.model.ts
export interface ApiFieldError {
  field?: string;
  message: string;
  code?: string | number;
}

export interface ApiError {
  status: number;
  error: string;
  message: string;
  path?: string;
  timestamp?: string;
  errors?: ApiFieldError[];
}

// Utilities to normalize backend 422 payloads into user-friendly strings
export function extractErrorMessages(err: any): string[] {
  const msgs: string[] = [];
  if (!err) return ["Erro desconhecido."];
  const body = err?.error ?? err;
  if (typeof body === "string") {
    msgs.push(body);
  } else {
    if (body?.message) msgs.push(body.message);
    if (Array.isArray(body?.errors)) {
      for (const fe of body.errors) {
        if (fe?.field) msgs.push(`${fe.field}: ${fe.message}`);
        else if (fe?.message) msgs.push(fe.message);
      }
    }
  }
  // Fallback to HTTP text
  if (msgs.length === 0 && err?.statusText) msgs.push(`${err.status} ${err.statusText}`);
  return msgs.length ? msgs : ["Falha na requisição."];
}
