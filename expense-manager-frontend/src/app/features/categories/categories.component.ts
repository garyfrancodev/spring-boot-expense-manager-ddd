import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '@core/services/auth.service';
import { CategoryService } from '@core/services/category.service';
import { Category, CreateCategoryRequest } from '@core/models/category.models';

@Component({
  selector: 'app-categories',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="page-container">
      <div class="page-header">
        <div>
          <h2>Categorías de Gastos e Ingresos</h2>
          <p class="page-sub">Organiza y segmenta tus movimientos financieros con etiquetas personalizadas</p>
        </div>
        <button class="btn btn-primary" (click)="openModal()" id="btn-add-cat">
          + Nueva Categoría
        </button>
      </div>

      @if (loading()) {
        <div class="loading-state">Cargando categorías...</div>
      } @else if (categories().length === 0) {
        <div class="empty-state glass-panel">
          <div class="empty-icon">🏷️</div>
          <h3>No tienes categorías creadas aún</h3>
          <p>Crea categorías como Alimentación, Transporte, Salario, etc.</p>
          <button class="btn btn-primary" (click)="openModal()">Crear Categoría Ahora</button>
        </div>
      } @else {
        <div class="categories-grid">
          @for (cat of categories(); track cat.id) {
            <div class="category-card glass-panel">
              <div class="cat-top">
                <div class="cat-icon-badge">{{ cat.categoryIcon || '📁' }}</div>
                <span class="badge" [ngClass]="cat.categoryType === 'INCOME' ? 'badge-income' : 'badge-expense'">
                  {{ cat.categoryType === 'INCOME' ? 'Ingreso' : 'Gasto' }}
                </span>
              </div>
              <div class="cat-body">
                <h4 class="cat-title">{{ cat.categoryName }}</h4>
                <p class="cat-desc">{{ cat.categoryDescription || 'Sin descripción' }}</p>
              </div>
            </div>
          }
        </div>
      }
    </div>

    <!-- MODAL: Crear Categoría -->
    @if (showModal) {
      <div class="modal-backdrop" (click)="closeModal()">
        <div class="modal-content" (click)="$event.stopPropagation()">
          <div class="modal-header">
            <h3>Nueva Categoría</h3>
            <button class="btn-close" (click)="closeModal()">✕</button>
          </div>
          <form (ngSubmit)="onCreate()">
            <div class="modal-body">
              @if (modalError) {
                <div class="alert alert-danger">{{ modalError }}</div>
              }
              <div class="form-group">
                <label class="form-label">Tipo de Categoría</label>
                <div class="type-switch">
                  <button type="button" class="type-btn" [class.active-expense]="catType === 'EXPENSE'" (click)="catType = 'EXPENSE'">
                    📉 Gasto
                  </button>
                  <button type="button" class="type-btn" [class.active-income]="catType === 'INCOME'" (click)="catType = 'INCOME'">
                    📈 Ingreso
                  </button>
                </div>
              </div>

              <div class="form-group">
                <label class="form-label">Nombre de Categoría</label>
                <input type="text" class="form-control" [(ngModel)]="name" name="name" required placeholder="Ej. Alimentación, Supermercado">
              </div>

              <div class="form-group">
                <label class="form-label">Emoji / Ícono</label>
                <div class="emoji-picker-row">
                  @for (emoji of defaultEmojis; track emoji) {
                    <button type="button" class="emoji-choice" [class.selected]="icon === emoji" (click)="icon = emoji">
                      {{ emoji }}
                    </button>
                  }
                </div>
                <input type="text" class="form-control" [(ngModel)]="icon" name="icon" placeholder="O escribe tu propio emoji/ícono" style="margin-top: 8px;">
              </div>

              <div class="form-group">
                <label class="form-label">Descripción</label>
                <input type="text" class="form-control" [(ngModel)]="desc" name="desc" placeholder="Ej. Gastos del supermercado y restaurantes">
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" (click)="closeModal()">Cancelar</button>
              <button type="submit" class="btn btn-primary" [disabled]="!name || modalLoading">
                {{ modalLoading ? 'Guardando...' : 'Crear Categoría' }}
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
    .categories-grid {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
      gap: 20px;
    }
    .category-card {
      padding: 22px;
      border-radius: var(--radius-xl);
      display: flex;
      flex-direction: column;
      gap: 16px;
      transition: transform var(--transition-fast), border-color var(--transition-fast);
    }
    .category-card:hover {
      transform: translateY(-2px);
      border-color: rgba(255, 255, 255, 0.18);
    }
    .cat-top {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }
    .cat-icon-badge {
      width: 44px;
      height: 44px;
      border-radius: var(--radius-lg);
      background: rgba(255, 255, 255, 0.06);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 22px;
      border: 1px solid var(--border-subtle);
    }
    .cat-title {
      font-size: 16px;
      font-weight: 700;
      color: var(--text-primary);
      margin-bottom: 4px;
    }
    .cat-desc {
      font-size: 13px;
      color: var(--text-secondary);
      line-height: 1.5;
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
    .emoji-picker-row {
      display: flex;
      gap: 8px;
      flex-wrap: wrap;
    }
    .emoji-choice {
      font-size: 20px;
      padding: 6px 10px;
      background: var(--bg-input);
      border: 1px solid var(--border-subtle);
      border-radius: var(--radius-sm);
      cursor: pointer;
      transition: all var(--transition-fast);
    }
    .emoji-choice:hover {
      background: rgba(255, 255, 255, 0.1);
    }
    .emoji-choice.selected {
      border-color: var(--accent-primary);
      background: rgba(99, 102, 241, 0.2);
    }
  `]
})
export class CategoriesComponent implements OnInit {
  readonly categories = signal<Category[]>([]);
  readonly loading = signal<boolean>(true);

  defaultEmojis = ['🍔', '🚗', '🏠', '💡', '🎮', '✈️', '💼', '🛒', '💊', '🎁'];

  showModal = false;
  modalLoading = false;
  modalError = '';

  catType = 'EXPENSE';
  name = '';
  icon = '🍔';
  desc = '';

  constructor(private authService: AuthService, private categoryService: CategoryService) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    const userId = this.authService.getUserId();
    if (!userId) return;

    this.loading.set(true);
    this.categoryService.getCategories(userId).subscribe({
      next: (data: Category[]) => {
        this.categories.set(data || []);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  openModal(): void {
    this.catType = 'EXPENSE';
    this.name = '';
    this.icon = '🍔';
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

    const req: CreateCategoryRequest = {
      categoryName: this.name,
      categoryIcon: this.icon || '📁',
      categoryDescription: this.desc,
      categoryType: this.catType,
      userId
    };

    this.categoryService.createCategory(req).subscribe({
      next: () => {
        this.modalLoading = false;
        this.closeModal();
        this.loadCategories();
      },
      error: (err: any) => {
        this.modalLoading = false;
        this.modalError = err.error?.message || 'Error al crear la categoría.';
      }
    });
  }
}
