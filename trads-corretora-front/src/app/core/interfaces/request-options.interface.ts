import { HttpParams } from "@angular/common/http";

export interface IRequestOptions {
    params?: HttpParams | { [param: string]: string | number | boolean | ReadonlyArray<string | number | boolean> };
}