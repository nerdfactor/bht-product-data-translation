import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class HttpService {

  url: string = 'http://localhost:8080/';
  constructor(protected httpClient: HttpClient) { }

  private getHeader(isoCode: string) {
    return new HttpHeaders().set('Accept-Language', isoCode);
  }

  get<T>(url: string, isoCode?: string) {
    let headers: HttpHeaders | null = null;
    if (isoCode)
      headers = this.getHeader(isoCode);
    return this.httpClient.get<T>(this.url + url, { headers: headers ?? undefined });
  }

  post<T>(url: string, payload: any, isoCode?: string) {
    let headers: HttpHeaders | null = null;
    if (isoCode)
      headers = this.getHeader(isoCode);
    return this.httpClient.post<T>(this.url + url, payload, { headers: headers ?? undefined});
  }

  put<T>(url: string, payload: any, isoCode?: string) {
    let headers: HttpHeaders | null = null;
    if (isoCode)
      headers = this.getHeader(isoCode);
    return this.httpClient.put<T>(this.url + url, payload, { headers: headers ?? undefined});
  }

  patch<T>(url: string, payload: any, isoCode?: string) {
    let headers: HttpHeaders | null = null;
    if (isoCode)
      headers = this.getHeader(isoCode);
    return this.httpClient.patch<T>(this.url + url, payload, { headers: headers ?? undefined});
  }

  delete(url: string, isoCode?: string) {
    let headers: HttpHeaders | null = null;
    if (isoCode)
      headers = this.getHeader(isoCode);
    return this.httpClient.delete(this.url + url, { headers: headers ?? undefined});
  }

}
