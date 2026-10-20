import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="auth-page">
      <div class="auth-card glass-panel">
        <div class="auth-header">
          <div class="brand-logo">✨</div>
          <h2>Crear Cuenta</h2>
          <p class="subtitle">Empieza a gestionar tus gastos con precisión DDD</p>
        </div>

        @if (errorMessage) {
          <div class="alert alert-danger">
            <span>⚠️</span>
            <span>{{ errorMessage }}</span>
          </div>
        }

        @if (successMessage) {
          <div class="alert alert-success">
            <span>✅</span>
            <span>{{ successMessage }}</span>
          </div>
        }

        <form (ngSubmit)="onSubmit()" #registerForm="ngForm">
          <div class="name-grid">
            <div class="form-group">
              <label class="form-label" for="reg-first-name">Nombre</label>
              <input
                type="text"
                id="reg-first-name"
                name="firstName"
                class="form-control"
                [(ngModel)]="firstName"
                required
                placeholder="Juan"
              />
            </div>

            <div class="form-group">
              <label class="form-label" for="reg-last-name">Apellido</label>
              <input
                type="text"
                id="reg-last-name"
                name="lastName"
                class="form-control"
                [(ngModel)]="lastName"
                required
                placeholder="Pérez"
              />
            </div>
          </div>

          <div class="form-group">
            <label class="form-label" for="reg-email">Correo Electrónico</label>
            <input
              type="email"
              id="reg-email"
              name="email"
              class="form-control"
              [(ngModel)]="email"
              required
              placeholder="juan.perez@example.com"
            />
          </div>

          <div class="form-group">
            <label class="form-label" for="reg-password">Contraseña</label>
            <input
              type="password"
              id="reg-password"
              name="password"
              class="form-control"
              [(ngModel)]="password"
              required
              placeholder="Mínimo 6 caracteres"
            />
          </div>

          <button
            type="submit"
            class="btn btn-primary btn-block"
            id="btn-submit-register"
            [disabled]="loading || !firstName || !lastName || !email || !password"
          >
            @if (loading) {
              <span>Creando cuenta...</span>
            } @else {
              <span>Registrarse Gratis</span>
            }
          </button>
        </form>

        <div class="auth-footer">
          <span>¿Ya tienes una cuenta?</span>
          <a routerLink="/login" class="link-login">Inicia sesión aquí</a>
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
      max-width: 480px;
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
    .name-grid {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 14px;
    }
    .btn-block {
      width: 100%;
      margin-top: 10px;
      padding: 12px;
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
    .link-login {
      font-weight: 600;
      color: var(--accent-primary);
    }
  `]
})
export class RegisterComponent {
  firstName = '';
  lastName = '';
  email = '';
  password = '';
  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    if (!this.firstName || !this.lastName || !this.email || !this.password) return;

    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.authService.register({
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      password: this.password
    }).subscribe({
      next: () => {
        this.loading = false;
        this.successMessage = '¡Cuenta creada con éxito! Redirigiendo al login...';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 1200);
      },
      error: (err: any) => {
        this.loading = false;
        if (err.error?.message) {
          this.errorMessage = err.error.message;
        } else {
          this.errorMessage = 'Ocurrió un error al crear la cuenta. Verifica los datos.';
        }
      }
    });
  }
}
