// src/app/features/superpowers/superpowers.component.ts
import { Component, inject, signal } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ReactiveFormsModule, FormBuilder, Validators } from "@angular/forms";
import { RouterModule } from "@angular/router";
import { SuperpowersService } from "./superpowers.service";
import { Superpower, SuperpowerCreateDTO, SuperpowerUpdateDTO } from "./superpower.model";

// PrimeNG 20.2
import { TableModule } from "primeng/table";
import { ButtonModule } from "primeng/button";
import { DialogModule } from "primeng/dialog";
import { ToolbarModule } from "primeng/toolbar";
import { InputTextModule } from "primeng/inputtext";
import { ToastModule } from "primeng/toast";
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { MessageService, ConfirmationService } from "primeng/api";
import { ProgressSpinnerModule } from "primeng/progressspinner";

import { extractErrorMessages } from "./api-error.model";

@Component({
  selector: "app-superpowers",
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
    ToastModule,
    ConfirmDialogModule,
    ProgressSpinnerModule
  ],
  providers: [MessageService, ConfirmationService],
  templateUrl: "./superpowers.component.html",
  styleUrls: ["./superpowers.component.scss"]
})
export class SuperpowersComponent {
  private svc = inject(SuperpowersService);
  private fb = inject(FormBuilder);
  private toast = inject(MessageService);
  private confirm = inject(ConfirmationService);

  items = signal<Superpower[]>([]);
  loading = signal<boolean>(false);
  dialogVisible = signal<boolean>(false);
  dialogTitle = signal<string>("Novo superpoder");
  editingId = signal<number | null>(null);
  submitting = signal<boolean>(false);

  form = this.fb.nonNullable.group({
    nome: this.fb.nonNullable.control("", [Validators.required, Validators.maxLength(120)]),
    descricao: this.fb.control<string | null>("", { validators: [] })
  });

  get f() { return this.form.controls; }

  constructor() { this.load(); }

  load() {
    this.loading.set(true);
    this.svc.list().subscribe({
      next: data => { this.items.set(data ?? []); this.loading.set(false); },
      error: err => {
        this.loading.set(false);
        this.toast.add({severity: "error", summary: "Falha ao listar", detail: extractErrorMessages(err).join(" • ")});
      }
    });
  }

  openNew() {
    this.form.reset();
    this.editingId.set(null);
    this.dialogTitle.set("Novo superpoder");
    this.dialogVisible.set(true);
  }

  openEdit(sp: Superpower) {
    this.editingId.set(sp.id);
    this.dialogTitle.set(`Editar #${sp.id}`);
    this.form.reset();
    this.form.patchValue({
      nome: sp.nome,
      descricao: sp.descricao ?? ""
    });
    this.dialogVisible.set(true);
  }

  hideDialog() { this.dialogVisible.set(false); }

  save() {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    const v = this.form.getRawValue();
    const payload: SuperpowerCreateDTO = { nome: v.nome.trim(), descricao: (v.descricao ?? "").trim() || null };

    this.submitting.set(true);
    const id = this.editingId();
    const obs = id == null ? this.svc.create(payload) : this.svc.update(id, payload as SuperpowerUpdateDTO);
    obs.subscribe({
      next: saved => {
        this.submitting.set(false);
        this.dialogVisible.set(false);
        this.toast.add({severity: "success", summary: id == null ? "Criado" : "Atualizado", detail: `${saved.nome} (#${saved.id})`});
        this.load();
      },
      error: err => {
        this.submitting.set(false);
        this.toast.add({severity: "error", summary: "Erro ao salvar", detail: extractErrorMessages(err).join(" • ")});
      }
    });
  }

  confirmDelete(sp: Superpower) {
    this.confirm.confirm({
      header: "Confirmar exclusão",
      message: `Excluir ${sp.nome} (#${sp.id})?`,
      acceptLabel: "Excluir",
      acceptButtonStyleClass: "p-button-danger",
      rejectLabel: "Cancelar",
      accept: () => this.remove(sp)
    });
  }

  private remove(sp: Superpower) {
    this.loading.set(true);
    this.svc.delete(sp.id).subscribe({
      next: () => { this.toast.add({severity: "success", summary: "Removido", detail: `${sp.nome} (#${sp.id})`}); this.load(); },
      error: err => {
        this.loading.set(false);
        this.toast.add({severity: "error", summary: "Erro ao remover", detail: extractErrorMessages(err).join(" • ")});
      }
    });
  }
}
