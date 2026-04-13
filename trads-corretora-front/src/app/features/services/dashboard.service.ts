import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  IPageResponse,
  IStage,
  IUsers,
  IVwGain,
  IVwLossConversion,
  IVwLoss,
  IVwTimeStage,
  IVwTransitions,
} from '../../core/interfaces/dashboard-views.interface';
import { HttpAbstract } from '../../core/abstract/http.abstract';
import { environment } from '../../env/enviroments';

@Injectable({
  providedIn: 'root',
})
export class DashboardService extends HttpAbstract {
  protected readonly baseUrl: string = environment.apiUrl;
  getGains(userId?: number, startDate?: string, endDate?: string): Observable<IPageResponse<IVwGain>> {
    const params = this.createParams(userId, startDate, endDate);
    return this.get<IPageResponse<IVwGain>>(`/dashboard/vw-gain`, { params });
  }

  getLosses(userId?: number, startDate?: string, endDate?: string): Observable<IPageResponse<IVwLoss>> {
    const params = this.createParams(userId, startDate, endDate);
    return this.get<IPageResponse<IVwLoss>>(`/dashboard/vw-loss`, { params });
  }

  getTimeStages(userId?: number, startDate?: string, endDate?: string): Observable<IPageResponse<IVwTimeStage>> {
    const params = this.createParams(userId, startDate, endDate);
    return this.get<IPageResponse<IVwTimeStage>>(`/dashboard/vw-time-stage`, { params });
  }

  getTransitions(userId?: number, startDate?: string, endDate?: string): Observable<IPageResponse<IVwTransitions>> {
    const params = this.createParams(userId, startDate, endDate);
    return this.get<IPageResponse<IVwTransitions>>(`/dashboard/vw-transitions`, { params });
  }

  getLossConversion(userId?: number, startDate?: string, endDate?: string): Observable<IVwLossConversion[]> {
    const params = this.createParams(userId, startDate, endDate);
    return this.get<IVwLossConversion[]>(`/dashboard/vw-loss-conversion`, { params });
  }

  getAllUsers(): Observable<IUsers[]> {
    return this.get<IUsers[]>(`/users/list`);
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
