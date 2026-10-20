import { Routes } from '@angular/router';
import { authGuard } from './core/auth.guard';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'dashboard'
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login.component').then((m) => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () =>
      import('./features/auth/register/register.component').then((m) => m.RegisterComponent)
  },
  {
    path: 'dashboard',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent)
  },
  {
    path: 'accounts',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/accounts/accounts.component').then((m) => m.AccountsComponent)
  },
  {
    path: 'transactions',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/transactions/transactions.component').then((m) => m.TransactionsComponent)
  },
  {
    path: 'transfers',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/transfers/transfers.component').then((m) => m.TransfersComponent)
  },
  {
    path: 'categories',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/categories/categories.component').then((m) => m.CategoriesComponent)
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];
