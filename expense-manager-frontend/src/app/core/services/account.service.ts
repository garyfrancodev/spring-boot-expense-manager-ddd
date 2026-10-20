import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Account, CreateAccountRequest } from '../models/account.models';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private apiUrl = `${environment.apiUrl}/account`;

  constructor(private http: HttpClient) {}

  getAccounts(userId: string): Observable<Account[]> {
    const params = new HttpParams().set('userId', userId);
    return this.http.get<Account[]>(this.apiUrl, { params });
  }

  getAmountTotal(userId: string): Observable<number> {
    const params = new HttpParams().set('userId', userId);
    return this.http.get<number>(`${this.apiUrl}/amount-total`, { params });
  }

  createAccount(request: CreateAccountRequest): Observable<string> {
    return this.http.post<string>(this.apiUrl, request);
  }
}
