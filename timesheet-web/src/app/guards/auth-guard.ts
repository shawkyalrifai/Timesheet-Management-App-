import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);

  if (typeof window === 'undefined') {
    return false;
  }
  const token = localStorage.getItem('jwt_token');

  if (token) {
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};
