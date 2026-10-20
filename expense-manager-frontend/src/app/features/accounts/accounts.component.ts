import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '@core/services/auth.service';
import { AccountService } from '@core/services/account.service';
import { Account, CreateAccountRequest } from '@core/models/account.models';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="page-container">
      <div class="page-header">
        <div>
          <h2>Gestión de Cuentas</h2>
          <p class="page-sub">Administra tus cuentas bancarias, billeteras digitales y tarjetas</p>
        </div>
        <button class="btn btn-primary" (click)="openModal()" id="btn-add-account">
          + Nueva Cuenta
        </button>
      </div>

      @if (loading()) {
        <div class="loading-state">Cargando cuentas...</div>
      } @else if (accounts().length === 0) {
        <div class="empty-state glass-panel">
          <div class="empty-icon">💳</div>
          <h3>No tienes cuentas creadas aún</h3>
          <p>Crea tu primera cuenta para empezar a gestionar tus balances y registrar transacciones.</p>
          <button class="btn btn-primary" (click)="openModal()">Crear Cuenta Ahora</button>
        </div>
      } @else {
        <div class="cards-grid">
          @for (acc of accounts(); track acc.id) {
            <div class="credit-card glass-panel">
              <div class="card-top">
                <span class="bank-chip">EMV</span>
                <span class="card-icon">🏦</span>
              </div>
              <div class="card-center">
                <span class="balance-title">Balance Actual</span>
                <h3 class="card-amount">{{ acc.amount | currency:'USD':'symbol':'1.2-2' }}</h3>
              </div>
              <div class="card-bottom">
                <div>
                  <span class="holder-label">Nombre de Cuenta</span>
                  <div class="holder-name">{{ acc.accountName }}</div>
                </div>
                <div class="card-type">{{ acc.description || 'General' }}</div>
              </div>
            </div>
          }
        </div>
      }
    </div>

    @if (showModal) {
      <div class="modal-backdrop" (click)="closeModal()">
        <div class="modal-content" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h3>Nueva Cuenta Bancaria</h3>
            <button class="btn-close" (click)="closeModal()">✕</button>
          </div>
          <form (ngSubmit)="onCreate()">
            <div class="modal-body">
              @if (modalError) {
                <div class="alert alert-danger">{{ modalError }}</div>
              }
              <div class="form-group">
                <label class="form-label">Nombre de Cuenta</label>
                <input type="text" class="form-control" [(ngModel)]="name" name="name" required placeholder="Ej. Ahorros Banco BCP">
              </div>
              <div class="form-group">
                <label class="form-label">Balance Inicial ($)</label>
                <input type="number" class="form-control" [(ngModel)]="amount" name="amount" min="0" step="0.01" required placeholder="0.00">
              </div>
              <div class="form-group">
                <label class="form-label">Descripción</label>
                <input type="text" class="form-control" [(ngModel)]="desc" name="desc" placeholder="Ej. Tarjeta de débito para gastos diarios">
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" (click)="closeModal()">Cancelar</button>
              <button type="submit" class="btn btn-primary" [disabled]="!name || modalLoading">
                {{ modalLoading ? 'Creando...' : 'Crear Cuenta' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    }
  `,
  styles: [`
    .page-container {
      max-width: 1280px;
      margin: 0 auto;
      padding: 36px 24px;
      display: flex;
      flex-direction: column;
      gap: 28px;
    }
    .page-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-wrap: wrap;
      gap: 16px;
    }
    .page-sub {
      color: var(--text-secondary);
      font-size: 14px;
      margin-top: 4px;
    }
    .cards-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
      gap: 24px;
    }
    .credit-card {
      padding: 26px;
      border-radius: var(--radius-xl);
      min-height: 200px;
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      background: linear-gradient(135deg, rgba(30, 41, 59, 0.8) 0%, rgba(15, 23, 42, 0.95) 100%);
      border: 1px solid rgba(255, 255, 255, 0.12);
      box-shadow: var(--shadow-lg);
      position: relative;
      overflow: hidden;
    }
    .credit-card::before {
      content: '';
      position: absolute;
      top: -30px;
      right: -30px;
      width: 120px;
      height: 120px;
      background: var(--accent-gradient);
      opacity: 0.15;
      border-radius: 50%;
      filter: blur(30px);
    }
    .card-top {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
    .bank-chip {
      font-size: 11px;
      font-weight: 700;
      letter-spacing: 0.1em;
      padding: 3px 8px;
      border-radius: 4px;
      background: rgba(255, 255, 255, 0.1);
      border: 1px solid rgba(255, 255, 255, 0.2);
    }
    .card-icon { font-size: 22px; }
    .balance-title {
      font-size: 12px;
      color: var(--text-secondary);
      text-transform: uppercase;
      letter-spacing: 0.05em;
    }
    .card-amount {
      font-family: var(--font-heading);
      font-size: 30px;
      font-weight: 800;
      color: #ffffff;
      margin-top: 4px;
    }
    .card-bottom {
      display: flex;
      align-items: flex-end;
      justify-content: space-between;
    }
    .holder-label {
      font-size: 10px;
      text-transform: uppercase;
      color: var(--text-muted);
      letter-spacing: 0.05em;
    }
    .holder-name {
      font-size: 15px;
      font-weight: 700;
      color: var(--text-primary);
    }
    .card-type {
      font-size: 12px;
      color: var(--text-secondary);
      max-width: 120px;
      text-align: right;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .empty-state {
      padding: 60px;
      text-align: center;
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 12px;
    }
    .empty-icon { font-size: 48px; }
    .loading-state {
      text-align: center;
      padding: 40px;
      color: var(--text-muted);
    }
  `]
})
export class AccountsComponent implements OnInit {
  readonly accounts = signal<Account[]>([]);
  readonly loading = signal<boolean>(true);

  showModal = false;
  modalLoading = false;
  modalError = '';

  name = '';
  amount = 0;
  desc = '';

  constructor(private authService: AuthService, private accountService: AccountService) {}

  ngOnInit(): void {
    this.loadAccounts();
  }

  loadAccounts(): void {
    const userId = this.authService.getUserId();
    if (!userId) return;

    this.loading.set(true);
    this.accountService.getAccounts(userId).subscribe({
      next: (data: Account[]) => {
        this.accounts.set(data || []);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  openModal(): void {
    this.name = '';
    this.amount = 0;
    this.desc = '';
    this.modalError = '';
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  onCreate(): void {
    const userId = this.authService.getUserId();
    if (!userId || !this.name) return;

    this.modalLoading = true;
    this.modalError = '';

    const req: CreateAccountRequest = {
      accountName: this.name,
      amount: this.amount,
      description: this.desc,
      userId
    };

    this.accountService.createAccount(req).subscribe({
      next: () => {
        this.modalLoading = false;
        this.closeModal();
        this.loadAccounts();
      },
      error: (err: any) => {
        this.modalLoading = false;
        this.modalError = err.error?.message || 'Error al crear la cuenta.';
      }
    });
  }
}
