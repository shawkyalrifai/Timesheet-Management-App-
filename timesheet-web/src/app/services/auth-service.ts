import { Injectable } from '@angular/core';
import {Observable, tap, throwError} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {LoginRequest} from '../login-request';
import {catchError} from 'rxjs/operators';
import {RegisterRequest} from '../register-request';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = 'http://localhost:8080/api/auth';
  private tokenKey = 'jwt_token';
  constructor(private http: HttpClient) {}

  login(data: LoginRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, data).pipe(
      tap((res: any) => {
        if (typeof window !== 'undefined' && window.localStorage) {
          if (res?.token) {
            localStorage.setItem('jwt_token', res.token);
          }
          if (data?.email) {
            localStorage.setItem('email', data.email);
          }
        }
      }),
      catchError((err) => throwError(() => err.error || 'Login failed'))
    );
  }

  register(data: RegisterRequest): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, data)
      .pipe(catchError((err) => throwError(() => err.error)));
  }

  logout(): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : ''
    });
    return this.http.post(`${this.baseUrl}/logout`, {}, { headers });
  }

  setToken(token: string) {
    localStorage.setItem(this.tokenKey, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  removeToken() {
    localStorage.removeItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
