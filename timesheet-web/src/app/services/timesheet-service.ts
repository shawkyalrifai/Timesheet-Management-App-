// src/app/services/timesheet.service.ts
import { Injectable } from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import {TimesheetRequest} from '../timesheet-request';


@Injectable({
  providedIn: 'root'
})
export class TimesheetService {
  private baseUrl = 'http://localhost:8080/api/timesheet';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('jwt_token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  getToday(): Observable<any> {
    const headers = this.getHeaders();
    const email = localStorage.getItem('email') || '';
    return this.http.get(`${this.baseUrl}/today`, { params: { email }, headers })
      .pipe(catchError(this.handleError));
  }

  getDay(date: string): Observable<any> {
    const headers = this.getHeaders();
    const email = localStorage.getItem('email') || '';
    return this.http.get(`${this.baseUrl}/day`, { params: { email, date }, headers })
      .pipe(catchError(this.handleError));
  }

  submitTimesheet(data: TimesheetRequest): Observable<any> {
    const headers = this.getHeaders();
    const email = localStorage.getItem('email') || '';
    const body = { ...data, email };
    return this.http.post(`${this.baseUrl}/submit`, body, { headers })
      .pipe(catchError(this.handleError));
  }



  private handleError(error: HttpErrorResponse) {
    console.error('Server error:', error);
    return throwError(() => error.error || 'Server error');
  }
}
