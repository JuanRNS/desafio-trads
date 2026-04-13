import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { RequestOptions } from '../interfaces/request-options.interface';
import { HTTP } from '../enums/http-methods.enum';

export abstract class HttpAbstract {
    constructor(
        private readonly _baseUrl: string,
        private readonly _http: HttpClient
    ) { }

    protected request<T>(
        method: HTTP,
        path: string,
        body?: any,
        options?: RequestOptions
    ): Observable<T> {
        const token = localStorage.getItem('token');
        let headers = new HttpHeaders();
        if (token) {
            headers = headers.set('Authorization', `Bearer ${token}`);
        }
        return this._http.request<T>(method, `${this._baseUrl}${path}`, {
            body,
            ...options,
            headers: headers
        });
    }

    public get<T>(path: string, options?: RequestOptions): Observable<T> {
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
