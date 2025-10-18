// src/app/features/heroes/heroes.component.ts
import { Component, inject, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule, FormBuilder, Validators } from "@angular/forms";
import { RouterModule } from "@angular/router";
import { HeroesService } from "./heroes.service";
import { Hero, HeroCreateDTO, HeroUpdateDTO } from "./hero.model";

// PrimeNG 20.2
import { TableModule } from "primeng/table";
import { ButtonModule } from "primeng/button";
import { DialogModule } from "primeng/dialog";
import { ToolbarModule } from "primeng/toolbar";
import { InputTextModule } from "primeng/inputtext";
import { DatePickerModule } from "primeng/datepicker";
import { InputNumberModule } from "primeng/inputnumber";
import { ToastModule } from "primeng/toast";
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { TagModule } from "primeng/tag";
import { MessageService, ConfirmationService } from "primeng/api";
import { ProgressSpinnerModule } from "primeng/progressspinner";

import { extractErrorMessages } from "./api-error.model";

@Component({
  selector: "app-heroes",
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    ReactiveFormsModule,
    TableModule,
    ButtonModule,
    DialogModule,
    ToolbarModule,
    InputTextModule,
    DatePickerModule,
    InputNumberModule,
    ToastModule,
    ConfirmDialogModule,
    TagModule,
    ProgressSpinnerModule
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: "./heroes.component.html",
  styleUrls: ["./heroes.component.scss"]
})
export class HeroesComponent {
  private svc = inject(HeroesService);
  private fb = inject(FormBuilder);
  private toast = inject(MessageService);
  private confirm = inject(ConfirmationService);

  heroes = signal<Hero[]>([]);
  loading = signal<boolean>(false);
  dialogVisible = signal<boolean>(false);
  dialogTitle = signal<string>("Novo herói");
  editingId = signal<number | null>(null);
  submitting = signal<boolean>(false);

  // Form com CSV para superpoderes (alinha com o HTML atualizado)
  form = this.fb.nonNullable.group({
    nome: this.fb.nonNullable.control("", [Validators.required, Validators.maxLength(120)]),
    nomeHeroi: this.fb.nonNullable.control("", [Validators.required, Validators.maxLength(120)]),
    dataNascimento: this.fb.control<Date | null>(null, { validators: [Validators.required] }),
    altura: this.fb.control<number | null>(null, { validators: [Validators.required, Validators.min(0.01)] }),
    peso: this.fb.control<number | null>(null, { validators: [Validators.required, Validators.min(0.01)] }),
    superpoderesCsv: this.fb.nonNullable.control("", [Validators.required]) // ← novo controle
  });

  // helper pra usar no template se quiser (ex.: f.superpoderesCsv)
  get f() { return this.form.controls; }

  constructor() {
    this.load();
  }

  load() {
    this.loading.set(true);
    this.svc.list().subscribe({
      next: data => {
        this.heroes.set(data ?? []);
        this.loading.set(false);
      },
      error: err => {
        this.loading.set(false);
        this.toast.add({ severity: "error", summary: "Falha ao listar", detail: extractErrorMessages(err).join(" • ") });
      }
    });
  }

  openNew() {
    this.form.reset();
    this.form.patchValue({ superpoderesCsv: "" });
    this.editingId.set(null);
    this.dialogTitle.set("Novo herói");
    this.dialogVisible.set(true);
  }

  openEdit(hero: Hero) {
    this.editingId.set(hero.id);
    this.dialogTitle.set(`Editar #${hero.id}`);
    const dt = hero.dataNascimento ? new Date(hero.dataNascimento) : null;
    this.form.reset();
    this.form.patchValue({
      nome: hero.nome,
      nomeHeroi: hero.nomeHeroi,
      dataNascimento: dt,
      altura: hero.altura,
      peso: hero.peso,
      superpoderesCsv: (hero.superpoderesIds ?? []).join(",")
    });
    this.dialogVisible.set(true);
  }

  hideDialog() {
    this.dialogVisible.set(false);
  }

  private toInstantZ(d: Date): string {
    // Garante Instant UTC (Z) independente do timezone do cliente
    return new Date(Date.UTC(d.getFullYear(), d.getMonth(), d.getDate(), 0, 0, 0)).toISOString();
  }

  private parseCsvToIds(csv: string | null | undefined): number[] {
    if (!csv) return [];
    return csv
      .split(",")
      .map(s => s.trim())
      .filter(s => s.length > 0)
      .map(n => Number(n))
      .filter(n => Number.isFinite(n));
  }

  save() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const v = this.form.getRawValue();
    const ids = this.parseCsvToIds(v.superpoderesCsv);

    if (ids.length === 0) {
      this.toast.add({ severity: "warn", summary: "Validação", detail: "Informe ao menos 1 ID de superpoder." });
      return;
    }

    const payload = {
      nome: v.nome.trim(),
      nomeHeroi: v.nomeHeroi.trim(),
      dataNascimento: this.toInstantZ(v.dataNascimento as Date),
      altura: Number(v.altura),
      peso: Number(v.peso),
      superpoderesIds: ids
    } as HeroCreateDTO;

    this.submitting.set(true);
    const id = this.editingId();
    const obs = id == null ? this.svc.create(payload) : this.svc.update(id, payload as HeroUpdateDTO);

    obs.subscribe({
      next: saved => {
        this.submitting.set(false);
        this.dialogVisible.set(false);
        this.toast.add({ severity: "success", summary: id == null ? "Criado" : "Atualizado", detail: `${saved.nomeHeroi} (#${saved.id})` });
        this.load();
      },
      error: err => {
        this.submitting.set(false);
        const details = extractErrorMessages(err).join(" • ");
        this.toast.add({ severity: "error", summary: "Erro ao salvar", detail: details });
      }
    });
  }

  confirmDelete(hero: Hero) {
    this.confirm.confirm({
      header: "Confirmar exclusão",
      message: `Excluir ${hero.nomeHeroi} (#${hero.id})?`,
      acceptLabel: "Excluir",
      acceptButtonStyleClass: "p-button-danger",
      rejectLabel: "Cancelar",
      accept: () => this.remove(hero)
    });
  }

  private remove(hero: Hero) {
    this.loading.set(true);
    this.svc.delete(hero.id).subscribe({
      next: () => {
        this.toast.add({ severity: "success", summary: "Removido", detail: `${hero.nomeHeroi} (#${hero.id})` });
        this.load();
      },
      error: err => {
        this.loading.set(false);
        this.toast.add({ severity: "error", summary: "Erro ao remover", detail: extractErrorMessages(err).join(" • ") });
      }
    });
  }
}
