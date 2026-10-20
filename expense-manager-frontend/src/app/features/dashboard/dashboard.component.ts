import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AuthService } from '@core/services/auth.service';
import { AccountService } from '@core/services/account.service';
import { TransactionService } from '@core/services/transaction.service';
import { CategoryService } from '@core/services/category.service';
import { TransferService } from '@core/services/transfer.service';
import { Account, CreateAccountRequest } from '@core/models/account.models';
import { Transaction, CreateTransactionRequest } from '@core/models/transaction.models';
import { Category } from '@core/models/category.models';
import { CreateTransferRequest } from '@core/models/transfer.models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="dashboard-page">
      <div class="dashboard-container">
        
        <!-- Welcome & Header Section -->
        <div class="welcome-header">
          <div>
            <h1 class="welcome-title">Hola, <span class="gradient-text">{{ getUserName() }}</span> 👋</h1>
            <p class="welcome-sub">Monitorea tus finanzas personales y balance de cuentas en tiempo real.</p>
          </div>
          <div class="header-actions">
            <button class="btn btn-secondary" (click)="openAccountModal()" id="btn-quick-account">
              + Nueva Cuenta
            </button>
            <button class="btn btn-secondary" (click)="openTransferModal()" id="btn-quick-transfer">
              ↔ Transferir
            </button>
            <button class="btn btn-primary" (click)="openTransactionModal()" id="btn-quick-transaction">
              + Registrar Movimiento
            </button>
          </div>
        </div>

        <!-- Metric Stat Cards -->
        <div class="stats-grid">
          <div class="stat-card glass-panel highlight-card">
            <div class="stat-icon-wrap bg-indigo">💰</div>
            <div class="stat-info">
              <span class="stat-label">Balance Total</span>
              <h2 class="stat-value">{{ totalBalance() | currency:'USD':'symbol':'1.2-2' }}</h2>
              <span class="stat-trend positive">● Sincronizado en tiempo real</span>
            </div>
          </div>

          <div class="stat-card glass-panel">
            <div class="stat-icon-wrap bg-emerald">📈</div>
            <div class="stat-info">
              <span class="stat-label">Ingresos Registrados</span>
              <h3 class="stat-value text-emerald">+{{ totalIncome() | currency:'USD':'symbol':'1.2-2' }}</h3>
              <span class="stat-detail">{{ incomeCount() }} movimientos</span>
            </div>
          </div>

          <div class="stat-card glass-panel">
            <div class="stat-icon-wrap bg-rose">📉</div>
            <div class="stat-info">
              <span class="stat-label">Gastos Registrados</span>
              <h3 class="stat-value text-rose">-{{ totalExpense() | currency:'USD':'symbol':'1.2-2' }}</h3>
              <span class="stat-detail">{{ expenseCount() }} movimientos</span>
            </div>
          </div>

          <div class="stat-card glass-panel">
            <div class="stat-icon-wrap bg-cyan">💳</div>
            <div class="stat-info">
              <span class="stat-label">Cuentas Bancarias</span>
              <h3 class="stat-value text-cyan">{{ accounts().length }}</h3>
              <span class="stat-detail">Cuentas activas</span>
            </div>
          </div>
        </div>

        <!-- Accounts Section -->
        <section class="section-block">
          <div class="section-header">
            <div>
              <h3 class="section-title">Tus Cuentas</h3>
              <p class="section-sub">Billeteras, cuentas de ahorro y corrientes</p>
            </div>
            <a routerLink="/accounts" class="link-more">Ver todas las cuentas →</a>
          </div>

          @if (loadingAccounts()) {
            <div class="loading-state">Cargando cuentas...</div>
          } @else if (accounts().length === 0) {
            <div class="empty-state glass-panel">
              <div class="empty-icon">💳</div>
              <h4>No tienes cuentas registradas aún</h4>
              <p>Crea tu primera cuenta para empezar a registrar ingresos y gastos.</p>
              <button class="btn btn-primary btn-sm" (click)="openAccountModal()">Crear Cuenta Ahora</button>
            </div>
          } @else {
            <div class="accounts-grid">
              @for (account of accounts(); track account.id) {
                <div class="account-card glass-panel">
                  <div class="account-chip">
                    <span class="account-type-icon">🏦</span>
                    <span class="account-card-name">{{ account.accountName }}</span>
                  </div>
                  <div class="account-balance-area">
                    <span class="balance-tag">Saldo Disponible</span>
                    <div class="account-amount">{{ account.amount | currency:'USD':'symbol':'1.2-2' }}</div>
                  </div>
                  <div class="account-footer-info">
                    <span class="account-desc">{{ account.description || 'Cuenta principal' }}</span>
                  </div>
                </div>
              }
            </div>
          }
        </section>

        <!-- Recent Transactions Section -->
        <section class="section-block">
          <div class="section-header">
            <div>
              <h3 class="section-title">Últimos Movimientos</h3>
              <p class="section-sub">Transacciones recientes en todas tus cuentas</p>
            </div>
            <a routerLink="/transactions" class="link-more">Ver historial completo →</a>
          </div>

          @if (loadingTransactions()) {
            <div class="loading-state">Cargando transacciones...</div>
          } @else if (transactions().length === 0) {
            <div class="empty-state glass-panel">
              <div class="empty-icon">📝</div>
              <h4>No hay transacciones registradas</h4>
              <p>Registra tu primer ingreso o gasto con el botón superior.</p>
              <button class="btn btn-primary btn-sm" (click)="openTransactionModal()">+ Registrar Movimiento</button>
            </div>
          } @else {
            <div class="transactions-table-card glass-panel">
              <table class="custom-table">
                <thead>
                  <tr>
                    <th>Descripción</th>
                    <th>Categoría</th>
                    <th>Cuenta</th>
                    <th>Fecha</th>
                    <th class="text-right">Monto</th>
                  </tr>
                </thead>
                <tbody>
                  @for (tx of recentTransactions(); track tx.id) {
                    <tr>
                      <td class="tx-desc-cell">
                        <div class="tx-icon" [ngClass]="tx.transactionType === 'INCOME' ? 'income-icon' : 'expense-icon'">
                          {{ tx.transactionType === 'INCOME' ? '↓' : '↑' }}
                        </div>
                        <div>
                          <div class="tx-title">{{ tx.description || 'Movimiento' }}</div>
                          <span class="badge" [ngClass]="tx.transactionType === 'INCOME' ? 'badge-income' : 'badge-expense'">
                            {{ tx.transactionType === 'INCOME' ? 'Ingreso' : 'Gasto' }}
                          </span>
                        </div>
                      </td>
                      <td>
                        <span class="category-pill">{{ getCategoryName(tx.categoryId) }}</span>
                      </td>
                      <td class="text-secondary">{{ getAccountName(tx.accountId) }}</td>
                      <td class="text-secondary">{{ tx.date | date:'dd MMM yyyy, HH:mm' }}</td>
                      <td class="text-right tx-amount-cell" [ngClass]="tx.transactionType === 'INCOME' ? 'text-emerald' : 'text-rose'">
                        {{ tx.transactionType === 'INCOME' ? '+' : '-' }}{{ tx.amount | currency:'USD':'symbol':'1.2-2' }}
                      </td>
                    </tr>
                  }
                </tbody>
              </table>
            </div>
          }
        </section>

      </div>
    </div>

    <!-- MODAL: Crear Cuenta -->
    @if (showAccountModal) {
      <div class="modal-backdrop" (click)="closeAccountModal()">
        <div class="modal-content" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h3>Nueva Cuenta Bancaria</h3>
            <button class="btn-close" (click)="closeAccountModal()">✕</button>
          </div>
          <form (ngSubmit)="onCreateAccount()">
            <div class="modal-body">
              @if (modalError) {
                <div class="alert alert-danger">{{ modalError }}</div>
              }
              <div class="form-group">
                <label class="form-label">Nombre de la Cuenta</label>
                <input type="text" class="form-control" [(ngModel)]="newAccountName" name="accName" required placeholder="Ej. Ahorros BCP, Billetera">
              </div>
              <div class="form-group">
                <label class="form-label">Balance Inicial ($)</label>
                <input type="number" class="form-control" [(ngModel)]="newAccountAmount" name="accAmount" min="0" step="0.01" required placeholder="0.00">
              </div>
              <div class="form-group">
                <label class="form-label">Descripción</label>
                <input type="text" class="form-control" [(ngModel)]="newAccountDesc" name="accDesc" placeholder="Ej. Cuenta de sueldo o emergencias">
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" (click)="closeAccountModal()">Cancelar</button>
              <button type="submit" class="btn btn-primary" [disabled]="!newAccountName || modalLoading">
                {{ modalLoading ? 'Creando...' : 'Crear Cuenta' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    }

    <!-- MODAL: Registrar Transacción -->
    @if (showTransactionModal) {
      <div class="modal-backdrop" (click)="closeTransactionModal()">
        <div class="modal-content" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h3>Registrar Movimiento</h3>
            <button class="btn-close" (click)="closeTransactionModal()">✕</button>
          </div>
          <form (ngSubmit)="onCreateTransaction()">
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
                <select class="form-control" [(ngModel)]="txAccountId" name="txAcc" required>
                  <option value="" disabled>Selecciona una cuenta</option>
                  @for (acc of accounts(); track acc.id) {
                    <option [value]="acc.id">{{ acc.accountName }} ({{ acc.amount | currency:'USD':'symbol':'1.2-2' }})</option>
                  }
                </select>
              </div>

              <div class="form-group">
                <label class="form-label">Categoría</label>
                <select class="form-control" [(ngModel)]="txCategoryId" name="txCat" required>
                  <option value="" disabled>Selecciona una categoría</option>
                  @for (cat of categories(); track cat.id) {
                    <option [value]="cat.id">{{ cat.categoryIcon }} {{ cat.categoryName }}</option>
                  }
                </select>
              </div>

              <div class="form-group">
                <label class="form-label">Monto ($)</label>
                <input type="number" class="form-control" [(ngModel)]="txAmount" name="txAmt" min="0.01" step="0.01" required placeholder="0.00">
              </div>

              <div class="form-group">
                <label class="form-label">Descripción</label>
                <input type="text" class="form-control" [(ngModel)]="txDescription" name="txDesc" placeholder="Ej. Almuerzo, Salario, Combustible">
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" (click)="closeTransactionModal()">Cancelar</button>
              <button type="submit" class="btn btn-primary" [disabled]="!txAccountId || !txCategoryId || !txAmount || modalLoading">
                {{ modalLoading ? 'Guardando...' : 'Guardar Transacción' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    }

    <!-- MODAL: Transferencia entre Cuentas -->
    @if (showTransferModal) {
      <div class="modal-backdrop" (click)="closeTransferModal()">
        <div class="modal-content" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h3>Transferir entre Cuentas</h3>
            <button class="btn-close" (click)="closeTransferModal()">✕</button>
          </div>
          <form (ngSubmit)="onCreateTransfer()">
            <div class="modal-body">
              @if (modalError) {
                <div class="alert alert-danger">{{ modalError }}</div>
              }
              <div class="form-group">
                <label class="form-label">Cuenta Origen (Débito)</label>
                <select class="form-control" [(ngModel)]="tfSourceId" name="tfSrc" required>
                  <option value="" disabled>Selecciona cuenta de origen</option>
                  @for (acc of accounts(); track acc.id) {
                    <option [value]="acc.id">{{ acc.accountName }} (Disp: {{ acc.amount | currency:'USD':'symbol':'1.2-2' }})</option>
                  }
                </select>
              </div>

              <div class="form-group">
                <label class="form-label">Cuenta Destino (Crédito)</label>
                <select class="form-control" [(ngModel)]="tfDestId" name="tfDst" required>
                  <option value="" disabled>Selecciona cuenta destino</option>
                  @for (acc of accounts(); track acc.id) {
                    @if (acc.id !== tfSourceId) {
                      <option [value]="acc.id">{{ acc.accountName }} (Disp: {{ acc.amount | currency:'USD':'symbol':'1.2-2' }})</option>
                    }
                  }
                </select>
              </div>

              <div class="form-group">
                <label class="form-label">Monto a Transferir ($)</label>
                <input type="number" class="form-control" [(ngModel)]="tfAmount" name="tfAmt" min="0.01" step="0.01" required placeholder="0.00">
              </div>

              <div class="form-group">
                <label class="form-label">Descripción</label>
                <input type="text" class="form-control" [(ngModel)]="tfDescription" name="tfDesc" placeholder="Ej. Traspaso a ahorros">
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" (click)="closeTransferModal()">Cancelar</button>
              <button type="submit" class="btn btn-primary" [disabled]="!tfSourceId || !tfDestId || !tfAmount || modalLoading">
                {{ modalLoading ? 'Transfiriendo...' : 'Confirmar Transferencia' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    }
  `,
  styles: [`
    .dashboard-page {
      padding: 36px 24px;
      min-height: calc(100vh - 70px);
    }
    .dashboard-container {
      max-width: 1280px;
      margin: 0 auto;
      display: flex;
      flex-direction: column;
      gap: 32px;
    }
    .welcome-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 20px;
      flex-wrap: wrap;
    }
    .welcome-title {
      font-size: 28px;
    }
    .welcome-sub {
      color: var(--text-secondary);
      font-size: 14px;
      margin-top: 4px;
    }
    .header-actions {
      display: flex;
      align-items: center;
      gap: 12px;
      flex-wrap: wrap;
    }

    /* Stats Grid */
    .stats-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
      gap: 18px;
    }
    .stat-card {
      padding: 24px;
      display: flex;
      align-items: flex-start;
      gap: 18px;
      border-radius: var(--radius-xl);
    }
    .highlight-card {
      background: linear-gradient(135deg, rgba(99, 102, 241, 0.12) 0%, rgba(139, 92, 246, 0.08) 100%);
      border: 1px solid rgba(99, 102, 241, 0.35);
    }
    .stat-icon-wrap {
      width: 48px;
      height: 48px;
      border-radius: var(--radius-lg);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 22px;
      flex-shrink: 0;
    }
    .bg-indigo { background: rgba(99, 102, 241, 0.15); border: 1px solid rgba(99, 102, 241, 0.3); }
    .bg-emerald { background: rgba(16, 185, 129, 0.15); border: 1px solid rgba(16, 185, 129, 0.3); }
    .bg-rose { background: rgba(244, 63, 94, 0.15); border: 1px solid rgba(244, 63, 94, 0.3); }
    .bg-cyan { background: rgba(6, 182, 212, 0.15); border: 1px solid rgba(6, 182, 212, 0.3); }
    
    .stat-label {
      font-size: 13px;
      color: var(--text-secondary);
      font-weight: 600;
    }
    .stat-value {
      font-size: 24px;
      font-weight: 700;
      margin: 4px 0;
      letter-spacing: -0.02em;
    }
    .stat-trend {
      font-size: 12px;
      color: var(--accent-primary);
      font-weight: 600;
    }
    .stat-detail {
      font-size: 12px;
      color: var(--text-muted);
    }

    .text-emerald { color: var(--success); }
    .text-rose { color: var(--danger); }
    .text-cyan { color: var(--info); }

    /* Sections */
    .section-block {
      display: flex;
      flex-direction: column;
      gap: 16px;
    }
    .section-header {
      display: flex;
      align-items: flex-end;
      justify-content: space-between;
    }
    .section-title {
      font-size: 19px;
      font-weight: 700;
    }
    .section-sub {
      font-size: 13px;
      color: var(--text-secondary);
    }
    .link-more {
      font-size: 13.5px;
      font-weight: 600;
      color: var(--accent-primary);
    }

    /* Accounts Grid */
    .accounts-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 18px;
    }
    .account-card {
      padding: 22px;
      border-radius: var(--radius-lg);
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      min-height: 140px;
      background: linear-gradient(135deg, rgba(22, 30, 46, 0.8) 0%, rgba(30, 41, 59, 0.6) 100%);
    }
    .account-chip {
      display: flex;
      align-items: center;
      gap: 8px;
    }
    .account-card-name {
      font-size: 15px;
      font-weight: 700;
    }
    .balance-tag {
      font-size: 11.5px;
      color: var(--text-secondary);
      text-transform: uppercase;
      letter-spacing: 0.04em;
    }
    .account-amount {
      font-family: var(--font-heading);
      font-size: 22px;
      font-weight: 700;
      color: #ffffff;
      margin-top: 2px;
    }
    .account-desc {
      font-size: 12px;
      color: var(--text-muted);
    }

    /* Transactions Table */
    .transactions-table-card {
      border-radius: var(--radius-xl);
      overflow-x: auto;
    }
    .custom-table {
      width: 100%;
      border-collapse: collapse;
      text-align: left;
      font-size: 14px;
    }
    .custom-table th {
      padding: 14px 20px;
      font-size: 12px;
      font-weight: 600;
      color: var(--text-secondary);
      text-transform: uppercase;
      letter-spacing: 0.04em;
      border-bottom: 1px solid var(--border-subtle);
    }
    .custom-table td {
      padding: 16px 20px;
      border-bottom: 1px solid var(--border-subtle);
    }
    .custom-table tr:last-child td {
      border-bottom: none;
    }
    .custom-table tr:hover {
      background: rgba(255, 255, 255, 0.02);
    }
    .tx-desc-cell {
      display: flex;
      align-items: center;
      gap: 14px;
    }
    .tx-icon {
      width: 34px;
      height: 34px;
      border-radius: var(--radius-md);
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 700;
      font-size: 16px;
    }
    .income-icon { background: var(--success-bg); color: var(--success); }
    .expense-icon { background: var(--danger-bg); color: var(--danger); }
    .tx-title {
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: 3px;
    }
    .category-pill {
      background: rgba(255, 255, 255, 0.05);
      border: 1px solid var(--border-subtle);
      padding: 3px 9px;
      border-radius: var(--radius-full);
      font-size: 12px;
    }
    .tx-amount-cell {
      font-family: var(--font-heading);
      font-weight: 700;
      font-size: 15px;
    }
    .text-right { text-align: right; }

    /* Empty and Loading States */
    .empty-state {
      padding: 48px;
      text-align: center;
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 12px;
    }
    .empty-icon { font-size: 40px; margin-bottom: 6px; }
    .loading-state {
      padding: 30px;
      text-align: center;
      color: var(--text-muted);
    }

    /* Modal Form Enhancements */
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
export class DashboardComponent implements OnInit {
  // Signals
  readonly accounts = signal<Account[]>([]);
  readonly transactions = signal<Transaction[]>([]);
  readonly categories = signal<Category[]>([]);
  readonly totalBalance = signal<number>(0);

  loadingAccounts = signal<boolean>(true);
  loadingTransactions = signal<boolean>(true);

  // Modals state
  showAccountModal = false;
  showTransactionModal = false;
  showTransferModal = false;
  modalLoading = false;
  modalError = '';

  // New Account inputs
  newAccountName = '';
  newAccountAmount = 0;
  newAccountDesc = '';

  // New Transaction inputs
  txType: 'EXPENSE' | 'INCOME' = 'EXPENSE';
  txAccountId = '';
  txCategoryId = '';
  txAmount = 0;
  txDescription = '';

  // New Transfer inputs
  tfSourceId = '';
  tfDestId = '';
  tfAmount = 0;
  tfDescription = '';

  constructor(
    private authService: AuthService,
    private accountService: AccountService,
    private transactionService: TransactionService,
    private categoryService: CategoryService,
    private transferService: TransferService
  ) {}

  ngOnInit(): void {
    this.refreshAllData();
  }

  getUserName(): string {
    const email = this.authService.currentUser()?.email || 'Usuario';
    return email.split('@')[0];
  }

  refreshAllData(): void {
    const userId = this.authService.getUserId();
    if (!userId) return;

    this.loadingAccounts.set(true);
    this.loadingTransactions.set(true);

    // Fetch Accounts
    this.accountService.getAccounts(userId).subscribe({
      next: (accs) => {
        this.accounts.set(accs || []);
        this.loadingAccounts.set(false);
      },
      error: () => this.loadingAccounts.set(false)
    });

    // Fetch Total Amount
    this.accountService.getAmountTotal(userId).subscribe({
      next: (total) => this.totalBalance.set(total || 0),
      error: () => this.totalBalance.set(0)
    });

    // Fetch Transactions
    this.transactionService.getTransactions(userId).subscribe({
      next: (txs) => {
        this.transactions.set(txs || []);
        this.loadingTransactions.set(false);
      },
      error: () => this.loadingTransactions.set(false)
    });

    // Fetch Categories
    this.categoryService.getCategories(userId).subscribe({
      next: (cats) => this.categories.set(cats || [])
    });
  }

  totalIncome(): number {
    return this.transactions()
      .filter((t) => t.transactionType === 'INCOME')
      .reduce((sum, t) => sum + (t.amount || 0), 0);
  }

  totalExpense(): number {
    return this.transactions()
      .filter((t) => t.transactionType === 'EXPENSE')
      .reduce((sum, t) => sum + (t.amount || 0), 0);
  }

  incomeCount(): number {
    return this.transactions().filter((t) => t.transactionType === 'INCOME').length;
  }

  expenseCount(): number {
    return this.transactions().filter((t) => t.transactionType === 'EXPENSE').length;
  }

  recentTransactions(): Transaction[] {
    return this.transactions().slice(0, 7);
  }

  getAccountName(accountId: string): string {
    const acc = this.accounts().find((a) => a.id === accountId);
    return acc ? acc.accountName : 'Cuenta';
  }

  getCategoryName(catId: string): string {
    const cat = this.categories().find((c) => c.id === catId);
    return cat ? `${cat.categoryIcon} ${cat.categoryName}` : 'General';
  }

  // Account Modal Actions
  openAccountModal(): void {
    this.newAccountName = '';
    this.newAccountAmount = 0;
    this.newAccountDesc = '';
    this.modalError = '';
    this.showAccountModal = true;
  }

  closeAccountModal(): void {
    this.showAccountModal = false;
  }

  onCreateAccount(): void {
    const userId = this.authService.getUserId();
    if (!userId || !this.newAccountName) return;

    this.modalLoading = true;
    this.modalError = '';

    const req: CreateAccountRequest = {
      accountName: this.newAccountName,
      amount: this.newAccountAmount,
      description: this.newAccountDesc,
      userId
    };

    this.accountService.createAccount(req).subscribe({
      next: () => {
        this.modalLoading = false;
        this.closeAccountModal();
        this.refreshAllData();
      },
      error: (err) => {
        this.modalLoading = false;
        this.modalError = err.error?.message || 'Error al crear la cuenta.';
      }
    });
  }

  // Transaction Modal Actions
  openTransactionModal(): void {
    this.txType = 'EXPENSE';
    this.txAccountId = this.accounts().length > 0 ? this.accounts()[0].id : '';
    this.txCategoryId = this.categories().length > 0 ? this.categories()[0].id : '';
    this.txAmount = 0;
    this.txDescription = '';
    this.modalError = '';
    this.showTransactionModal = true;
  }

  closeTransactionModal(): void {
    this.showTransactionModal = false;
  }

  onCreateTransaction(): void {
    const userId = this.authService.getUserId();
    if (!userId || !this.txAccountId || !this.txAmount) return;

    this.modalLoading = true;
    this.modalError = '';

    const req: CreateTransactionRequest = {
      accountId: this.txAccountId,
      categoryId: this.txCategoryId,
      amount: this.txAmount,
      transactionType: this.txType,
      description: this.txDescription,
      date: new Date().toISOString(),
      userId
    };

    this.transactionService.createTransaction(req).subscribe({
      next: () => {
        this.modalLoading = false;
        this.closeTransactionModal();
        this.refreshAllData();
      },
      error: (err: any) => {
        this.modalLoading = false;
        this.modalError = err.error?.message || 'Error al registrar movimiento.';
      }
    });
  }

  // Transfer Modal Actions
  openTransferModal(): void {
    this.tfSourceId = this.accounts().length > 0 ? this.accounts()[0].id : '';
    this.tfDestId = this.accounts().length > 1 ? this.accounts()[1].id : '';
    this.tfAmount = 0;
    this.tfDescription = '';
    this.modalError = '';
    this.showTransferModal = true;
  }

  closeTransferModal(): void {
    this.showTransferModal = false;
  }

  onCreateTransfer(): void {
    const userId = this.authService.getUserId();
    if (!userId || !this.tfSourceId || !this.tfDestId || !this.tfAmount) return;

    if (this.tfSourceId === this.tfDestId) {
      this.modalError = 'Las cuentas origen y destino deben ser diferentes.';
      return;
    }

    this.modalLoading = true;
    this.modalError = '';

    const req: CreateTransferRequest = {
      sourceAccountId: this.tfSourceId,
      destinationAccountId: this.tfDestId,
      amount: this.tfAmount,
      description: this.tfDescription || 'Transferencia entre cuentas',
      date: new Date().toISOString(),
      userId
    };

    this.transferService.createTransfer(req).subscribe({
      next: () => {
        this.modalLoading = false;
        this.closeTransferModal();
        this.refreshAllData();
      },
      error: (err: any) => {
        this.modalLoading = false;
        this.modalError = err.error?.message || 'Error al procesar la transferencia.';
      }
    });
  }
}
