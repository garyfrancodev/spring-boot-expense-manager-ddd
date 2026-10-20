import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest, UserProfile } from '../models/auth.models';

const AUTH_STORAGE_KEY = 'expense_flow_auth';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/auth`;
  
  // Reactive Signal for authenticated state
  readonly currentUser = signal<AuthResponse | null>(this.loadStoredAuth());
  readonly isAuthenticated = computed(() => !!this.currentUser());

  constructor(private http: HttpClient, private router: Router) {}

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((res) => {
        this.saveAuth(res);
      })
    );
  }

  register(userData: RegisterRequest): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/register`, userData);
  }

  getUserProfile(userId: string): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${environment.apiUrl}/user/${userId}`);
  }

  logout(): void {
    localStorage.removeItem(AUTH_STORAGE_KEY);
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return this.currentUser()?.jwt || null;
  }

  getUserId(): string | null {
    return this.currentUser()?.userId || null;
  }

  private saveAuth(auth: AuthResponse): void {
    localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(auth));
    this.currentUser.set(auth);
  }

  private loadStoredAuth(): AuthResponse | null {
    try {
      const stored = localStorage.getItem(AUTH_STORAGE_KEY);
      return stored ? JSON.parse(stored) : null;
    } catch {
      return null;
    }
  }
}
