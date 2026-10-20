import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Transfer, CreateTransferRequest } from '../models/transfer.models';

@Injectable({
  providedIn: 'root'
})
export class TransferService {
  private apiUrl = `${environment.apiUrl}/transfer`;

  constructor(private http: HttpClient) {}

  getTransfers(userId: string): Observable<Transfer[]> {
    const params = new HttpParams().set('userId', userId);
    return this.http.get<Transfer[]>(this.apiUrl, { params });
  }

  createTransfer(request: CreateTransferRequest): Observable<string> {
    return this.http.post<string>(this.apiUrl, request);
  }
}
