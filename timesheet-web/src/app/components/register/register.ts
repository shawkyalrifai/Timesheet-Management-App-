import { Component } from '@angular/core';
import {AuthService} from '../../services/auth-service';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {NgIf} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  standalone:true,
  selector: 'app-register',
  imports: [
    FormsModule, ReactiveFormsModule, NgIf, RouterLink
  ],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {
  registerForm: FormGroup;
  message: string = '';

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.registerForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      mobile: [''],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.registerForm.invalid) return;

    this.authService.register(this.registerForm.value).subscribe({
      next: (res) => {
        this.message = 'Registration successful!';
        this.registerForm.reset();
      },
      error: (err) => {
        this.message = err.message || 'Registration failed';
      }
    });
  }
}
