import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { TimesheetService } from '../../services/timesheet-service';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-timesheet',
  templateUrl: './timesheet.html',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule]
})
export class TimesheetComponent {
  timesheetForm: FormGroup;
  fetchForm: FormGroup;

  message: string = '';
  todayRecord: any = null;
  dayRecord: any = null;
  private userEmail: string | null = null;

  constructor(
    private fb: FormBuilder,
    private timesheetService: TimesheetService,
    private router: Router,
    private authService: AuthService
  ) {
    if (typeof window !== 'undefined' && window.localStorage) {
      this.userEmail = localStorage.getItem('email');
    } else {
      this.userEmail = null;
    }
    this.timesheetForm = this.fb.group({
      date: ['', Validators.required],
      loginTime: [''],
      logoutTime: ['']
    });

    this.fetchForm = this.fb.group({
      date: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.timesheetForm.invalid) return;

    const data = this.timesheetForm.value;

    data.loginTime = data.loginTime?.trim() || null;
    data.logoutTime = data.logoutTime?.trim() || null;

    // Make sure both aren't filled
    if (data.loginTime && data.logoutTime) {
      this.message = 'Cannot submit login and logout together';
      return;
    }

    this.timesheetService.submitTimesheet(data).subscribe({
      next: () => {
        this.message = 'Timesheet submitted successfully!';
        this.timesheetForm.reset();
      },
      error: (err) => {

        this.message = err.error?.message || err.error?.error || 'Error submitting timesheet';
      }
    });
  }


  fetchToday() {
    if (!this.userEmail) {
      this.message = 'User email not found. Please log in again.';
      return;
    }

    this.timesheetService.getToday().subscribe({
      next: (res) => {
        this.todayRecord = res;
        this.message = '';
      },
      error: (err) => {
        this.message = err?.error?.message || err?.message || 'Error fetching today\'s timesheet';
      }
    });
  }

  fetchDay() {
    if (this.fetchForm.invalid) {
      this.message = 'Date is required';
      return;
    }

    const date = this.fetchForm.get('date')?.value;
    if (!this.userEmail) {
      this.message = 'User email not found. Please log in again.';
      return;
    }

    this.timesheetService.getDay(date).subscribe({
      next: (res) => {
        this.dayRecord = res;
        this.message = '';
      },
      error: (err) => {
        this.message = err?.error?.message || err?.message || 'Error fetching this day\'s timesheet';
      }
    });
  }

  onLogout() {
    this.authService.logout().subscribe({
      next: (res) => {
        console.log('Logged out from server:', res);
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('email');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Logout error:', err);
        localStorage.removeItem('jwt_token');
        localStorage.removeItem('email');
        this.router.navigate(['/login']);
      }
    });
  }
}
