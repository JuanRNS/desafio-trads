import { Component, ChangeDetectionStrategy, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../features/services/auth.service';

@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HeaderComponent {
  public authService = inject(AuthService);
  private router = inject(Router);
  public userEmail = this.authService.currentUserEmail();
  public userRole = this.authService.currentUserRole();

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}

