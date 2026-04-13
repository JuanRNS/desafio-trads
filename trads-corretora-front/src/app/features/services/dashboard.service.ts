import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  PageResponse,
  StageDTO,
  UsersDTO,
  VwGainDTO,
  VwLossConversion,
  VwLossDTO,
  VwTimeStageDTO,
  VwTransitionsDTO,
} from '../../core/interfaces/dashboard-views.interface';
import { HttpAbstract } from '../../core/abstract/http.abstract';
import { environment } from '../../env/enviroments.dev';

@Injectable({
  providedIn: 'root',
})
export class DashboardService extends HttpAbstract {
  constructor() {
    super(environment.apiUrl, inject(HttpClient));
  }
  getGains(userId?: number, startDate?: string, endDate?: string): Observable<PageResponse<VwGainDTO>> {
    const params = this.createParams(userId, startDate, endDate);
    return this.get<PageResponse<VwGainDTO>>(`/dashboard/vw-gain`, { params });
  }

  getLosses(userId?: number, startDate?: string, endDate?: string): Observable<PageResponse<VwLossDTO>> {
    const params = this.createParams(userId, startDate, endDate);
    return this.get<PageResponse<VwLossDTO>>(`/dashboard/vw-loss`, { params });
  }

  getTimeStages(userId?: number, startDate?: string, endDate?: string): Observable<PageResponse<VwTimeStageDTO>> {
    const params = this.createParams(userId, startDate, endDate);
    return this.get<PageResponse<VwTimeStageDTO>>(`/dashboard/vw-time-stage`, { params });
  }

  getTransitions(userId?: number, startDate?: string, endDate?: string): Observable<PageResponse<VwTransitionsDTO>> {
    const params = this.createParams(userId, startDate, endDate);
    return this.get<PageResponse<VwTransitionsDTO>>(`/dashboard/vw-transitions`, { params });
  }

  getLossConversion(userId?: number, startDate?: string, endDate?: string): Observable<VwLossConversion[]> {
    const params = this.createParams(userId, startDate, endDate);
    return this.get<VwLossConversion[]>(`/dashboard/vw-loss-conversion`, { params });
  }

  getAllUsers(): Observable<UsersDTO[]> {
    return this.get<UsersDTO[]>(`/users/list`);
  }

  private createParams(userId?: number, startDate?: string, endDate?: string): HttpParams {
    let params = new HttpParams();

    if (userId) params = params.set('userId', userId.toString());
    if (startDate) {
      const start = new Date(startDate);
      start.setHours(0, 0, 0, 0);
      params = params.set('startDate', start.toISOString().split('T')[0]);
    }
    if (endDate) {
      const end = new Date(endDate);
      end.setHours(23, 59, 59, 999);
      params = params.set('endDate', end.toISOString().split('T')[0]);
    }
    params = params.set('size', '1000');
    params = params.set('page', '0');
    return params;
  }
}
