import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Category, CreateCategoryRequest } from '../models/category.models';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiUrl = `${environment.apiUrl}/category`;

  constructor(private http: HttpClient) {}

  getCategories(userId: string): Observable<Category[]> {
    const params = new HttpParams().set('userId', userId);
    return this.http.get<Category[]>(this.apiUrl, { params });
  }

  createCategory(request: CreateCategoryRequest): Observable<string> {
    return this.http.post<string>(this.apiUrl, request);
  }
}
