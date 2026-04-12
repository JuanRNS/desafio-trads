import { Routes } from '@angular/router';
import { LoginComponent } from './features/views/login/login.component';
import { authGuard } from './core/guards/auth.guard';
import { DashboardComponent } from './features/views/dashboard/dashboard.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { 
    path: '', 
    component: DashboardComponent,
    canActivate: [authGuard]
  },
  { path: '**', redirectTo: '' }
];
