import { inject, Injectable, signal } from '@angular/core';
import { HttpAbstract } from '../../core/abstract/http.abstract';
import { AuthRequest, AuthResponse, DecodedToken } from '../../core/interfaces/auth.interface';
import { Observable, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService extends HttpAbstract {
  public currentUserRole = signal<string | null>(null);
  public currentUserEmail = signal<string | null>(null);


  constructor() {
    super('http://localhost:8080', inject(HttpClient));
    this.hydrateUserContext();
  }

  public login(credentials: AuthRequest): Observable<AuthResponse> {
    return this.post<AuthResponse>(`/auth/login`, credentials).pipe(
      tap((response) => this.handleAuthentication(response.token))
    );
  }

  public logout(): void {
    localStorage.removeItem('token');
    this.currentUserRole.set(null);
    this.currentUserEmail.set(null);
  }

  public isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) return false;

    const decoded = this.decodeToken(token);
    if (decoded?.exp && decoded.exp * 1000 < Date.now()) {
      this.logout();
      return false;
    }

    return true;
  }

  public getToken(): string | null {
    return localStorage.getItem('token');
  }

  private handleAuthentication(token: string): void {
    localStorage.setItem('token', token);
    this.hydrateUserContext();
  }

  private hydrateUserContext(): void {
    const token = this.getToken();
    if (token) {
      const decoded = this.decodeToken(token);
      if (decoded) {
        if (decoded.role) this.currentUserRole.set(decoded.role);
        if (decoded.sub) this.currentUserEmail.set(decoded.sub);
      }
    }
  }

  private decodeToken(token: string): DecodedToken | null {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
      }).join(''));

      return JSON.parse(jsonPayload) as DecodedToken;
    } catch (e) {
      console.error('Invalid token format', e);
      return null;
    }
  }
}
