import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { RequestOptions } from '../interfaces/request-options.interface';
import { HTTP } from '../enums/http-methods.enum';



export abstract class HttpAbstract {
    protected readonly http = inject(HttpClient);

    protected request<T>(
        method: HTTP,
        url: string,
        body?: any,
        options?: RequestOptions
    ): Observable<T> {
        return this.http.request<T>(method, url, {
            body,
            ...options
        });
    }

    public get<T>(url: string, options?: RequestOptions): Observable<T> {
        return this.request<T>(HTTP.GET, url, undefined, options);
    }

    public post<T>(url: string, body: any, options?: RequestOptions): Observable<T> {
        return this.request<T>(HTTP.POST, url, body, options);
    }

    public put<T>(url: string, body: any, options?: RequestOptions): Observable<T> {
        return this.request<T>(HTTP.PUT, url, body, options);
    }

    public delete<T>(url: string, options?: RequestOptions): Observable<T> {
        return this.request<T>(HTTP.DELETE, url, undefined, options);
    }
}
