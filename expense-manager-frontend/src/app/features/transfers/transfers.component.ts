import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '@core/services/auth.service';
import { TransferService } from '@core/services/transfer.service';
import { AccountService } from '@core/services/account.service';
import { Transfer, CreateTransferRequest } from '@core/models/transfer.models';
import { Account } from '@core/models/account.models';

@Component({
  selector: 'app-transfers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="page-container">
      <div class="page-header">
        <div>
          <h2>Transferencias entre Cuentas</h2>
          <p class="page-sub">Transfiere saldos de forma instantánea y atómica con consistencia ACID</p>
        </div>
        <button class="btn btn-primary" (click)="openModal()" id="btn-add-transfer">
          ↔ Nueva Transferencia
        </button>
      </div>

      @if (loading()) {
        <div class="loading-state">Cargando transferencias...</div>
      } @else if (transfers().length === 0) {
        <div class="empty-state glass-panel">
          <div class="empty-icon">🔄</div>
          <h3>No has realizado transferencias aún</h3>
          <p>Mueve dinero entre tus cuentas de ahorro, corriente o billeteras fácilmente.</p>
          <button class="btn btn-primary" (click)="openModal()">Hacer Primera Transferencia</button>
        </div>
      } @else {
        <div class="table-card glass-panel">
          <table class="custom-table">
            <thead>
              <tr>
                <th>Operación</th>
                <th>Cuenta Origen</th>
                <th>Cuenta Destino</th>
                <th>Detalle</th>
                <th>Fecha</th>
                <th class="text-right">Monto Transferido</th>
              </tr>
            </thead>
            <tbody>
              @for (tf of transfers(); track tf.id) {
                <tr>
                  <td>
                    <div class="transfer-icon-pill">↔</div>
                  </td>
                  <td class="font-semibold text-rose">
                    {{ getAccountName(tf.sourceAccountId) }}
                  </td>
                  <td class="font-semibold text-emerald">
                    {{ getAccountName(tf.destinationAccountId) }}
                  </td>
                  <td class="text-secondary">{{ tf.description || 'Transferencia interna' }}</td>
                  <td class="text-secondary">{{ tf.date | date:'dd MMM yyyy, HH:mm' }}</td>
                  <td class="text-right tx-amount text-primary">
                    {{ tf.amount | currency:'USD':'symbol':'1.2-2' }}
                  </td>
                </tr>
              }
            </tbody>
          </table>
        </div>
      }
    </div>

    <!-- MODAL: Transferencia -->
    @if (showModal) {
      <div class="modal-backdrop" (click)="closeModal()">
        <div class="modal-content" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h3>Nueva Transferencia</h3>
            <button class="btn-close" (click)="closeModal()">✕</button>
          </div>
          <form (ngSubmit)="onCreate()">
            <div class="modal-body">
              @if (modalError) {
                <div class="alert alert-danger">{{ modalError }}</div>
              }
              <div class="form-group">
                <label class="form-label">Cuenta de Origen (Débito)</label>
                <select class="form-control" [(ngModel)]="sourceId" name="src" required>
                  <option value="" disabled>Selecciona cuenta a debitar</option>
                  @for (acc of accounts(); track acc.id) {
                    <option [value]="acc.id">{{ acc.accountName }} (Disp: {{ acc.amount | currency:'USD':'symbol':'1.2-2' }})</option>
                  }
                </select>
              </div>

              <div class="form-group">
                <label class="form-label">Cuenta de Destino (Crédito)</label>
                <select class="form-control" [(ngModel)]="destId" name="dst" required>
                  <option value="" disabled>Selecciona cuenta a acreditar</option>
                  @for (acc of accounts(); track acc.id) {
                    @if (acc.id !== sourceId) {
                      <option [value]="acc.id">{{ acc.accountName }} (Disp: {{ acc.amount | currency:'USD':'symbol':'1.2-2' }})</option>
                    }
                  }
                </select>
              </div>

              <div class="form-group">
                <label class="form-label">Monto a Transferir ($)</label>
                <input type="number" class="form-control" [(ngModel)]="amount" name="amount" min="0.01" step="0.01" required placeholder="0.00">
              </div>

              <div class="form-group">
                <label class="form-label">Descripción</label>
                <input type="text" class="form-control" [(ngModel)]="desc" name="desc" placeholder="Ej. Ahorro quincenal">
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" (click)="closeModal()">Cancelar</button>
              <button type="submit" class="btn btn-primary" [disabled]="!sourceId || !destId || !amount || modalLoading">
                {{ modalLoading ? 'Procesando...' : 'Confirmar Transferencia' }}
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
    .table-card {
      border-radius: var(--radius-xl);
      overflow-x: auto;
    }
    .custom-table {
      width: 100%;
      border-collapse: collapse;
      font-size: 14px;
      text-align: left;
    }
    .custom-table th {
      padding: 16px 22px;
      font-size: 12px;
      font-weight: 600;
      color: var(--text-secondary);
      text-transform: uppercase;
      border-bottom: 1px solid var(--border-subtle);
    }
    .custom-table td {
      padding: 16px 22px;
      border-bottom: 1px solid var(--border-subtle);
    }
    .custom-table tr:hover {
      background: rgba(255, 255, 255, 0.02);
    }
    .transfer-icon-pill {
      width: 32px;
      height: 32px;
      border-radius: var(--radius-md);
      background: rgba(99, 102, 241, 0.15);
      color: #818cf8;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 700;
    }
    .font-semibold { font-weight: 600; }
    .text-emerald { color: var(--success); }
    .text-rose { color: var(--danger); }
    .text-primary { color: var(--text-primary); }
    .text-right { text-align: right; }
    .tx-amount {
      font-family: var(--font-heading);
      font-weight: 700;
      font-size: 15px;
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
export class TransfersComponent implements OnInit {
  readonly transfers = signal<Transfer[]>([]);
  readonly accounts = signal<Account[]>([]);
  readonly loading = signal<boolean>(true);

  showModal = false;
  modalLoading = false;
  modalError = '';

  sourceId = '';
  destId = '';
  amount = 0;
  desc = '';

  constructor(
    private authService: AuthService,
    private transferService: TransferService,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    const userId = this.authService.getUserId();
    if (!userId) return;

    this.loading.set(true);
    this.transferService.getTransfers(userId).subscribe({
      next: (tfs: Transfer[]) => {
        this.transfers.set(tfs || []);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });

    this.accountService.getAccounts(userId).subscribe({
      next: (accs: Account[]) => this.accounts.set(accs || [])
    });
  }

  getAccountName(accountId: string): string {
    const acc = this.accounts().find((a) => a.id === accountId);
    return acc ? acc.accountName : 'Cuenta';
  }

  openModal(): void {
    this.sourceId = this.accounts().length > 0 ? this.accounts()[0].id : '';
    this.destId = this.accounts().length > 1 ? this.accounts()[1].id : '';
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
    if (!userId || !this.sourceId || !this.destId || !this.amount) return;

    if (this.sourceId === this.destId) {
      this.modalError = 'Las cuentas de origen y destino deben ser distintas.';
      return;
    }

    this.modalLoading = true;
    this.modalError = '';

    const req: CreateTransferRequest = {
      sourceAccountId: this.sourceId,
      destinationAccountId: this.destId,
      amount: this.amount,
      description: this.desc || 'Transferencia',
      date: new Date().toISOString(),
      userId
    };

    this.transferService.createTransfer(req).subscribe({
      next: () => {
        this.modalLoading = false;
        this.closeModal();
        this.loadData();
      },
      error: (err: any) => {
        this.modalLoading = false;
        this.modalError = err.error?.message || 'Error al procesar la transferencia.';
      }
    });
  }
}
