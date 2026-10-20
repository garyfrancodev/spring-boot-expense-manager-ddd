import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterModule],
  template: `
    <header class="app-header">
      <div class="header-container">
        <div class="logo-area" routerLink="/dashboard">
          <div class="logo-icon">⚡</div>
          <span class="logo-text">Expense<span class="gradient-text">Flow</span></span>
          <span class="logo-badge">DDD Core</span>
        </div>

        @if (authService.isAuthenticated()) {
          <nav class="nav-links">
            <a routerLink="/dashboard" routerLinkActive="active" class="nav-link" id="nav-dashboard">
              📊 Dashboard
            </a>
            <a routerLink="/accounts" routerLinkActive="active" class="nav-link" id="nav-accounts">
              💳 Cuentas
            </a>
            <a routerLink="/transactions" routerLinkActive="active" class="nav-link" id="nav-transactions">
              💸 Transacciones
            </a>
            <a routerLink="/transfers" routerLinkActive="active" class="nav-link" id="nav-transfers">
              ↔ Transferencias
            </a>
            <a routerLink="/categories" routerLinkActive="active" class="nav-link" id="nav-categories">
              🏷 Categorías
            </a>
          </nav>

          <div class="user-area">
            <div class="user-chip">
              <div class="avatar">{{ getUserInitials() }}</div>
              <span class="user-email">{{ authService.currentUser()?.email }}</span>
            </div>
            <button class="btn btn-secondary btn-sm" (click)="onLogout()" id="btn-logout" title="Cerrar sesión">
              Salir ⎋
            </button>
          </div>
        } @else {
          <div class="auth-actions">
            <a routerLink="/login" class="btn btn-secondary btn-sm">Iniciar Sesión</a>
            <a routerLink="/register" class="btn btn-primary btn-sm">Crear Cuenta</a>
          </div>
        }
      </div>
    </header>
  `,
  styles: [`
    .app-header {
      background: rgba(10, 14, 23, 0.85);
      backdrop-filter: blur(16px);
      -webkit-backdrop-filter: blur(16px);
      border-bottom: 1px solid var(--border-subtle);
      position: sticky;
      top: 0;
      z-index: 100;
    }
    .header-container {
      max-width: 1280px;
      margin: 0 auto;
      padding: 0 24px;
      height: 70px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      gap: 20px;
    }
    .logo-area {
      display: flex;
      align-items: center;
      gap: 10px;
      cursor: pointer;
      user-select: none;
    }
    .logo-icon {
      width: 38px;
      height: 38px;
      border-radius: var(--radius-md);
      background: var(--accent-gradient);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 20px;
      box-shadow: 0 4px 15px rgba(99, 102, 241, 0.4);
    }
    .logo-text {
      font-family: var(--font-heading);
      font-size: 20px;
      font-weight: 800;
      letter-spacing: -0.02em;
    }
    .logo-badge {
      font-size: 10px;
      font-weight: 700;
      letter-spacing: 0.05em;
      text-transform: uppercase;
      background: rgba(99, 102, 241, 0.15);
      color: #a5b4fc;
      border: 1px solid rgba(99, 102, 241, 0.3);
      padding: 2px 7px;
      border-radius: var(--radius-full);
    }
    .nav-links {
      display: flex;
      align-items: center;
      gap: 6px;
    }
    .nav-link {
      padding: 8px 14px;
      font-size: 13.5px;
      font-weight: 600;
      color: var(--text-secondary);
      border-radius: var(--radius-md);
      transition: all var(--transition-fast);
      display: flex;
      align-items: center;
      gap: 6px;
    }
    .nav-link:hover {
      color: var(--text-primary);
      background: rgba(255, 255, 255, 0.05);
    }
    .nav-link.active {
      color: #ffffff;
      background: rgba(99, 102, 241, 0.18);
      border: 1px solid rgba(99, 102, 241, 0.3);
    }
    .user-area {
      display: flex;
      align-items: center;
      gap: 12px;
    }
    .user-chip {
      display: flex;
      align-items: center;
      gap: 9px;
      padding: 5px 12px 5px 5px;
      background: rgba(255, 255, 255, 0.03);
      border: 1px solid var(--border-subtle);
      border-radius: var(--radius-full);
    }
    .avatar {
      width: 28px;
      height: 28px;
      border-radius: var(--radius-full);
      background: var(--accent-gradient);
      color: #ffffff;
      font-size: 11px;
      font-weight: 700;
      display: flex;
      align-items: center;
      justify-content: center;
      text-transform: uppercase;
    }
    .user-email {
      font-size: 12.5px;
      color: var(--text-secondary);
      max-width: 150px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .btn-sm {
      padding: 6px 12px;
      font-size: 12.5px;
    }
    @media (max-width: 900px) {
      .nav-links {
        display: none;
      }
    }
  `]
})
export class NavbarComponent {
  constructor(public authService: AuthService) {}

  getUserInitials(): string {
    const email = this.authService.currentUser()?.email || 'U';
    return email.substring(0, 2).toUpperCase();
  }

  onLogout(): void {
    this.authService.logout();
  }
}
