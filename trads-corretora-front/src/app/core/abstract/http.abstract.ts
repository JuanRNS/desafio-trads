import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { IRequestOptions } from '../interfaces/request-options.interface';
import { HTTP } from '../enums/http-methods.enum';

export abstract class HttpAbstract {
    protected readonly http = inject(HttpClient);
    protected abstract readonly baseUrl: string;
    protected request<T>(
        method: HTTP,
        path: string,
        body?: any,
        options?: IRequestOptions
    ): Observable<T> {
        const token = localStorage.getItem('token');
        let headers = new HttpHeaders();
        if (token) {
            headers = headers.set('Authorization', `Bearer ${token}`);
        }
        return this.http.request<T>(method, `${this.baseUrl}${path}`, {
            body,
            ...options,
            headers: headers
        });
    }

    public get<T>(path: string, options?: IRequestOptions): Observable<T> {
        return this.request<T>(HTTP.GET, path, undefined, options);
    }

    public post<T>(path: string, body: any): Observable<T> {
        return this.request<T>(HTTP.POST, path, body);
    }

    public put<T>(path: string, body: any): Observable<T> {
        return this.request<T>(HTTP.PUT, path, body);
    }

    public delete<T>(path: string): Observable<T> {
        return this.request<T>(HTTP.DELETE, path, undefined,);
    }
}
