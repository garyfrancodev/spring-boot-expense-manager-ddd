import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="auth-page">
      <div class="auth-card glass-panel">
        <div class="auth-header">
          <div class="brand-logo">⚡</div>
          <h2>Iniciar Sesión</h2>
          <p class="subtitle">Accede a tu panel financiero ExpenseFlow</p>
        </div>

        @if (errorMessage) {
          <div class="alert alert-danger" id="login-error-alert">
            <span>⚠️</span>
            <span>{{ errorMessage }}</span>
          </div>
        }

        <form (ngSubmit)="onSubmit()" #loginForm="ngForm">
          <div class="form-group">
            <label class="form-label" for="login-email">Correo Electrónico</label>
            <input
              type="email"
              id="login-email"
              name="email"
              class="form-control"
              [(ngModel)]="email"
              required
              placeholder="tu@email.com"
              autocomplete="email"
            />
          </div>

          <div class="form-group">
            <label class="form-label" for="login-password">Contraseña</label>
            <input
              type="password"
              id="login-password"
              name="password"
              class="form-control"
              [(ngModel)]="password"
              required
              placeholder="••••••••"
              autocomplete="current-password"
            />
          </div>

          <button
            type="submit"
            class="btn btn-primary btn-block"
            id="btn-submit-login"
            [disabled]="loading || !email || !password"
          >
            @if (loading) {
              <span>Conectando...</span>
            } @else {
              <span>Ingresar al Sistema →</span>
            }
          </button>
        </form>

        <div class="quick-test-section">
          <button type="button" class="btn btn-secondary btn-sm btn-block" (click)="fillDemoUser()">
            ⚡ Usar credenciales de prueba
          </button>
        </div>

        <div class="auth-footer">
          <span>¿No tienes una cuenta aún?</span>
          <a routerLink="/register" class="link-register">Crear cuenta gratis</a>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .auth-page {
      min-height: calc(100vh - 70px);
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 30px 20px;
    }
    .auth-card {
      width: 100%;
      max-width: 440px;
      padding: 36px;
      border-radius: var(--radius-xl);
    }
    .auth-header {
      text-align: center;
      margin-bottom: 28px;
    }
    .brand-logo {
      width: 50px;
      height: 50px;
      border-radius: var(--radius-lg);
      background: var(--accent-gradient);
      display: inline-flex;
      align-items: center;
      justify-content: center;
      font-size: 24px;
      margin-bottom: 14px;
      box-shadow: 0 4px 20px rgba(99, 102, 241, 0.4);
    }
    .subtitle {
      font-size: 13.5px;
      color: var(--text-secondary);
      margin-top: 6px;
    }
    .btn-block {
      width: 100%;
      margin-top: 10px;
      padding: 12px;
    }
    .quick-test-section {
      margin-top: 16px;
      padding-top: 16px;
      border-top: 1px dashed var(--border-subtle);
    }
    .auth-footer {
      margin-top: 24px;
      text-align: center;
      font-size: 13.5px;
      color: var(--text-secondary);
      display: flex;
      flex-direction: column;
      gap: 6px;
    }
    .link-register {
      font-weight: 600;
      color: var(--accent-primary);
    }
  `]
})
export class LoginComponent {
  email = '';
  password = '';
  loading = false;
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  fillDemoUser(): void {
    this.email = 'admin@example.com';
    this.password = 'Password123!';
  }

  onSubmit(): void {
    if (!this.email || !this.password) return;

    this.loading = true;
    this.errorMessage = '';

    this.authService.login({ email: this.email, password: this.password }).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/dashboard']);
      },
      error: (err: any) => {
        this.loading = false;
        if (err.status === 401) {
          this.errorMessage = 'Credenciales inválidas. Verifica tu correo o contraseña.';
        } else if (err.error?.message) {
          this.errorMessage = err.error.message;
        } else {
          this.errorMessage = 'Error de conexión con el API Gateway.';
        }
      }
    });
  }
}
