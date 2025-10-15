import { Routes } from '@angular/router';

import {Register} from './components/register/register';
import {TimesheetComponent} from './components/timesheet/timesheet';
import {LoginComponent} from './components/login/login';
import {authGuard} from './guards/auth-guard';


export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: Register },
  { path: 'timesheet', component: TimesheetComponent , canActivate: [authGuard], },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' } // fallback

];
