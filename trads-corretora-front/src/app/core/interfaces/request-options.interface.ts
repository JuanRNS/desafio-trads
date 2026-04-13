import { HttpParams } from "@angular/common/http";

export interface RequestOptions {
    params?: HttpParams | { [param: string]: string | number | boolean | ReadonlyArray<string | number | boolean> };
}