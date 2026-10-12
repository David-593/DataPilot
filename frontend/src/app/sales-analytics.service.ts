import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SalesSummary } from './sales-summary.model';

@Injectable({ providedIn: 'root' })
export class SalesAnalyticsService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/analytics/sales';

  getSales(from: string, to: string): Observable<SalesSummary> {
    const params = new HttpParams().set('from', from).set('to', to);
    return this.http.get<SalesSummary>(this.apiUrl, { params });
  }
}
