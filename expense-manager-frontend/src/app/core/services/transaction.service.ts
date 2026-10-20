import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Transaction, CreateTransactionRequest } from '../models/transaction.models';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private apiUrl = `${environment.apiUrl}/transaction`;

  constructor(private http: HttpClient) {}

  getTransactions(userId: string): Observable<Transaction[]> {
    const params = new HttpParams().set('userId', userId);
    return this.http.get<Transaction[]>(this.apiUrl, { params });
  }

  getTransactionsByPeriod(userId: string, startDate: string, endDate: string): Observable<Transaction[]> {
    const params = new HttpParams()
      .set('userId', userId)
      .set('startDate', startDate)
      .set('endDate', endDate);
    return this.http.get<Transaction[]>(`${this.apiUrl}/period`, { params });
  }

  createTransaction(request: CreateTransactionRequest): Observable<string> {
    return this.http.post<string>(this.apiUrl, request);
  }
}
