import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '@core/services/auth.service';
import { TransactionService } from '@core/services/transaction.service';
import { AccountService } from '@core/services/account.service';
import { CategoryService } from '@core/services/category.service';
import { Transaction, CreateTransactionRequest } from '@core/models/transaction.models';
import { Account } from '@core/models/account.models';
import { Category } from '@core/models/category.models';

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="page-container">
      <div class="page-header">
        <div>
          <h2>Movimientos y Transacciones</h2>
          <p class="page-sub">Historial detallado de ingresos y gastos clasificados</p>
        </div>
        <button class="btn btn-primary" (click)="openModal()" id="btn-add-tx">
          + Nuevo Movimiento
        </button>
      </div>

      <!-- Filter Bar -->
      <div class="filter-bar glass-panel">
        <div class="filter-group">
          <label class="filter-label">Tipo</label>
          <div class="filter-pills">
            <button class="filter-pill" [class.active]="typeFilter === 'ALL'" (click)="typeFilter = 'ALL'">Todos</button>
            <button class="filter-pill" [class.active]="typeFilter === 'INCOME'" (click)="typeFilter = 'INCOME'">Ingresos</button>
            <button class="filter-pill" [class.active]="typeFilter === 'EXPENSE'" (click)="typeFilter = 'EXPENSE'">Gastos</button>
          </div>
        </div>

        <div class="filter-group">
          <label class="filter-label">Fecha Inicio</label>
          <input type="date" class="form-control filter-input" [(ngModel)]="startDate" (change)="onDateFilterChange()">
        </div>

        <div class="filter-group">
          <label class="filter-label">Fecha Fin</label>
          <input type="date" class="form-control filter-input" [(ngModel)]="endDate" (change)="onDateFilterChange()">
        </div>

        @if (startDate || endDate) {
          <button class="btn btn-secondary btn-sm" (click)="clearDateFilter()">Limpiar Filtros</button>
        }
      </div>

      <!-- Table Section -->
      @if (loading()) {
        <div class="loading-state">Cargando transacciones...</div>
      } @else if (filteredTransactions().length === 0) {
        <div class="empty-state glass-panel">
          <div class="empty-icon">📂</div>
          <h3>No se encontraron transacciones</h3>
          <p>No hay registros que coincidan con los filtros seleccionados.</p>
        </div>
      } @else {
        <div class="table-card glass-panel">
          <table class="custom-table">
            <thead>
              <tr>
                <th>Tipo</th>
                <th>Descripción</th>
                <th>Categoría</th>
                <th>Cuenta</th>
                <th>Fecha</th>
                <th class="text-right">Monto</th>
              </tr>
            </thead>
            <tbody>
              @for (tx of filteredTransactions(); track tx.id) {
                <tr>
                  <td>
                    <span class="badge" [ngClass]="tx.transactionType === 'INCOME' ? 'badge-income' : 'badge-expense'">
                      {{ tx.transactionType === 'INCOME' ? 'Ingreso' : 'Gasto' }}
                    </span>
                  </td>
                  <td class="tx-title-cell">{{ tx.description || 'Sin descripción' }}</td>
                  <td>
                    <span class="category-pill">{{ getCategoryName(tx.categoryId) }}</span>
                  </td>
                  <td class="text-secondary">{{ getAccountName(tx.accountId) }}</td>
                  <td class="text-secondary">{{ tx.date | date:'dd MMM yyyy, HH:mm' }}</td>
                  <td class="text-right tx-amount" [ngClass]="tx.transactionType === 'INCOME' ? 'text-emerald' : 'text-rose'">
                    {{ tx.transactionType === 'INCOME' ? '+' : '-' }}{{ tx.amount | currency:'USD':'symbol':'1.2-2' }}
                  </td>
                </tr>
              }
            </tbody>
          </table>
        </div>
      }
    </div>

    <!-- MODAL: Registrar Transacción -->
    @if (showModal) {
      <div class="modal-backdrop" (click)="closeModal()">
        <div class="modal-content" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h3>Registrar Movimiento</h3>
            <button class="btn-close" (click)="closeModal()">✕</button>
          </div>
          <form (ngSubmit)="onCreate()">
            <div class="modal-body">
              @if (modalError) {
                <div class="alert alert-danger">{{ modalError }}</div>
              }
              <div class="form-group">
                <label class="form-label">Tipo de Movimiento</label>
                <div class="type-switch">
                  <button type="button" class="type-btn" [class.active-expense]="txType === 'EXPENSE'" (click)="txType = 'EXPENSE'">
                    📉 Gasto
                  </button>
                  <button type="button" class="type-btn" [class.active-income]="txType === 'INCOME'" (click)="txType = 'INCOME'">
                    📈 Ingreso
                  </button>
                </div>
              </div>

              <div class="form-group">
                <label class="form-label">Cuenta</label>
                <select class="form-control" [(ngModel)]="accountId" name="accountId" required>
                  <option value="" disabled>Selecciona una cuenta</option>
                  @for (acc of accounts(); track acc.id) {
                    <option [value]="acc.id">{{ acc.accountName }} (Disp: {{ acc.amount | currency:'USD':'symbol':'1.2-2' }})</option>
                  }
                </select>
              </div>

              <div class="form-group">
                <label class="form-label">Categoría</label>
                <select class="form-control" [(ngModel)]="categoryId" name="categoryId" required>
                  <option value="" disabled>Selecciona una categoría</option>
                  @for (cat of categories(); track cat.id) {
                    <option [value]="cat.id">{{ cat.categoryIcon }} {{ cat.categoryName }}</option>
                  }
                </select>
              </div>

              <div class="form-group">
                <label class="form-label">Monto ($)</label>
                <input type="number" class="form-control" [(ngModel)]="amount" name="amount" min="0.01" step="0.01" required placeholder="0.00">
              </div>

              <div class="form-group">
                <label class="form-label">Descripción</label>
                <input type="text" class="form-control" [(ngModel)]="desc" name="desc" placeholder="Detalle del gasto o ingreso">
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" (click)="closeModal()">Cancelar</button>
              <button type="submit" class="btn btn-primary" [disabled]="!accountId || !categoryId || !amount || modalLoading">
                {{ modalLoading ? 'Guardando...' : 'Guardar Movimiento' }}
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
    .filter-bar {
      padding: 18px 24px;
      display: flex;
      align-items: center;
      gap: 20px;
      flex-wrap: wrap;
    }
    .filter-group {
      display: flex;
      align-items: center;
      gap: 10px;
    }
    .filter-label {
      font-size: 12px;
      font-weight: 600;
      color: var(--text-secondary);
      text-transform: uppercase;
    }
    .filter-pills {
      display: flex;
      background: var(--bg-primary);
      padding: 3px;
      border-radius: var(--radius-md);
      border: 1px solid var(--border-subtle);
    }
    .filter-pill {
      padding: 6px 14px;
      font-size: 13px;
      font-weight: 600;
      background: transparent;
      border: none;
      color: var(--text-secondary);
      border-radius: var(--radius-sm);
      cursor: pointer;
      transition: all var(--transition-fast);
    }
    .filter-pill.active {
      background: var(--accent-primary);
      color: #ffffff;
    }
    .filter-input {
      padding: 8px 12px;
      font-size: 13px;
      width: auto;
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
    .tx-title-cell {
      font-weight: 600;
      color: var(--text-primary);
    }
    .category-pill {
      background: rgba(255, 255, 255, 0.05);
      border: 1px solid var(--border-subtle);
      padding: 3px 9px;
      border-radius: var(--radius-full);
      font-size: 12px;
    }
    .tx-amount {
      font-family: var(--font-heading);
      font-weight: 700;
      font-size: 15px;
    }
    .text-emerald { color: var(--success); }
    .text-rose { color: var(--danger); }
    .text-right { text-align: right; }
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
    .type-switch {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 10px;
    }
    .type-btn {
      padding: 10px;
      background: var(--bg-input);
      border: 1px solid var(--border-subtle);
      color: var(--text-secondary);
      border-radius: var(--radius-md);
      font-weight: 600;
      cursor: pointer;
      transition: all var(--transition-fast);
    }
    .active-expense {
      background: var(--danger-bg);
      border-color: var(--danger);
      color: var(--danger);
    }
    .active-income {
      background: var(--success-bg);
      border-color: var(--success);
      color: var(--success);
    }
  `]
})
export class TransactionsComponent implements OnInit {
  readonly transactions = signal<Transaction[]>([]);
  readonly accounts = signal<Account[]>([]);
  readonly categories = signal<Category[]>([]);
  readonly loading = signal<boolean>(true);

  typeFilter: 'ALL' | 'INCOME' | 'EXPENSE' = 'ALL';
  startDate = '';
  endDate = '';

  showModal = false;
  modalLoading = false;
  modalError = '';

  txType: 'EXPENSE' | 'INCOME' = 'EXPENSE';
  accountId = '';
  categoryId = '';
  amount = 0;
  desc = '';

  constructor(
    private authService: AuthService,
    private transactionService: TransactionService,
    private accountService: AccountService,
    private categoryService: CategoryService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    const userId = this.authService.getUserId();
    if (!userId) return;

    this.loading.set(true);

    this.transactionService.getTransactions(userId).subscribe({
      next: (txs: Transaction[]) => {
        this.transactions.set(txs || []);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });

    this.accountService.getAccounts(userId).subscribe({
      next: (accs: Account[]) => this.accounts.set(accs || [])
    });

    this.categoryService.getCategories(userId).subscribe({
      next: (cats: Category[]) => this.categories.set(cats || [])
    });
  }

  onDateFilterChange(): void {
    const userId = this.authService.getUserId();
    if (!userId) return;

    if (this.startDate && this.endDate) {
      this.loading.set(true);
      const startIso = new Date(this.startDate).toISOString();
      const endIso = new Date(this.endDate).toISOString();
      this.transactionService.getTransactionsByPeriod(userId, startIso, endIso).subscribe({
        next: (txs: Transaction[]) => {
          this.transactions.set(txs || []);
          this.loading.set(false);
        },
        error: () => this.loading.set(false)
      });
    }
  }

  clearDateFilter(): void {
    this.startDate = '';
    this.endDate = '';
    this.loadData();
  }

  filteredTransactions(): Transaction[] {
    let list = this.transactions();
    if (this.typeFilter !== 'ALL') {
      list = list.filter((t) => t.transactionType === this.typeFilter);
    }
    return list;
  }

  getAccountName(accountId: string): string {
    const acc = this.accounts().find((a) => a.id === accountId);
    return acc ? acc.accountName : 'Cuenta';
  }

  getCategoryName(catId: string): string {
    const cat = this.categories().find((c) => c.id === catId);
    return cat ? `${cat.categoryIcon} ${cat.categoryName}` : 'General';
  }

  openModal(): void {
    this.txType = 'EXPENSE';
    this.accountId = this.accounts().length > 0 ? this.accounts()[0].id : '';
    this.categoryId = this.categories().length > 0 ? this.categories()[0].id : '';
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
    if (!userId || !this.accountId || !this.amount) return;

    this.modalLoading = true;
    this.modalError = '';

    const req: CreateTransactionRequest = {
      accountId: this.accountId,
      categoryId: this.categoryId,
      amount: this.amount,
      transactionType: this.txType,
      description: this.desc,
      date: new Date().toISOString(),
      userId
    };

    this.transactionService.createTransaction(req).subscribe({
      next: () => {
        this.modalLoading = false;
        this.closeModal();
        this.loadData();
      },
      error: (err: any) => {
        this.modalLoading = false;
        this.modalError = err.error?.message || 'Error al guardar la transacción.';
      }
    });
  }
}
